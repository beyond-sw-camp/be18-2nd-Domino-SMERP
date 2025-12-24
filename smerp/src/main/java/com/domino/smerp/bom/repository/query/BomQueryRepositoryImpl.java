package com.domino.smerp.bom.repository.query;

import com.domino.smerp.bom.dto.request.SearchBomRequest;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.entity.QBom;
import com.domino.smerp.bom.entity.QBomClosure;
import com.domino.smerp.item.QItem;
import com.domino.smerp.item.QItemStatus;
import com.domino.smerp.item.constants.ItemStatusStatus;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class BomQueryRepositoryImpl implements BomQueryRepository {

  private final JPAQueryFactory queryFactory;

  // BOM 첫화면 페이징
  @Override
  @Transactional(readOnly = true)
  public Page<BomListResponse> searchBoms(final SearchBomRequest request, final Pageable pageable) {
    QItem item = QItem.item;
    QBom bom = QBom.bom;
    QItemStatus itemStatus = QItemStatus.itemStatus;
    QBomClosure bc = QBomClosure.bomClosure;
    QItem child = new QItem("child");

    List<BomListResponse> results = queryFactory
        .select(Projections.fields(BomListResponse.class,
            bom.bomId.as("bomId"),
            item.itemId.as("itemId"),
            item.name.as("itemName"),
            item.specification.as("specification"),
            item.itemStatus.status.stringValue().as("itemStatus"),
            // hasBom → 자식 BOM 존재 여부
            ExpressionUtils.as(
                JPAExpressions.selectOne()
                    .from(bom)
                    .where(bom.parentItem.eq(item))
                    .exists(),
                "hasBom"
            ),
            // materialCount → 자식 BOM 중 원재료 개수
            ExpressionUtils.as(
                JPAExpressions.select(bc.count())
                    .from(bc)
                    .join(child).on(child.itemId.eq(bc.id.descendantItemId))
                    .where(
                        bc.id.ancestorItemId.eq(item.itemId),
                        child.itemStatus.status.eq(ItemStatusStatus.RAW_MATERIAL)
                    ),
                "materialCount"
            )
        ))
        .from(item)
        .leftJoin(bom).on(bom.parentItem.eq(item))
        .join(item.itemStatus, itemStatus)
        .where(
            statusEq(request.getItemStatus()),
            nameContains(request.getItemName()),
            specificationContains(request.getSpecification()),
            hasBomTrue(item, bom)
        )
        .groupBy(item.itemId)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // 전체 개수 카운트
    JPAQuery<Long> countQuery = queryFactory
        .select(item.count())
        .from(item)
        .join(item.itemStatus, itemStatus)
        .where(
            statusEq(request.getItemStatus()),
            nameContains(request.getItemName()),
            specificationContains(request.getSpecification()),
            hasBomTrue(item, bom)
        );

    return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
  }


  // === 검색 조건 ===
  private BooleanExpression statusEq(final String status) {
    if (status == null || status.isEmpty()) {
      return null;
    }
    ItemStatusStatus statusEnum = ItemStatusStatus.fromLabel(status);
    return QItem.item.itemStatus.status.eq(statusEnum);
  }

  private BooleanExpression nameContains(final String name) {
    return (name == null || name.isEmpty()) ? null : QItem.item.name.contains(name);
  }

  private BooleanExpression specificationContains(final String specification) {
    return (specification == null || specification.isEmpty()) ? null
        : QItem.item.specification.contains(specification);
  }

  private BooleanExpression hasBomTrue(final QItem item, final QBom bom) {
    return JPAExpressions.selectOne()
        .from(bom)
        .where(bom.parentItem.eq(item))
        .exists();
  }

}
