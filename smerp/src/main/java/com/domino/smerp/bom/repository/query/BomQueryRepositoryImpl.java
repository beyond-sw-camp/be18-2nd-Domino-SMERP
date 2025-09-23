package com.domino.smerp.bom.repository.query;

import com.domino.smerp.bom.dto.request.SearchBomRequest;
import com.domino.smerp.bom.entity.BomClosure;
import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.entity.QBom;
import com.domino.smerp.bom.entity.QBomClosure;
import com.domino.smerp.bom.entity.QBomCostCache;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.QItem;
import com.domino.smerp.item.QItemStatus;
import com.domino.smerp.item.constants.ItemStatusStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
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

  @Override
  @Transactional(readOnly = true)
  public Page<BomCostCache> searchBoms(final SearchBomRequest request, final Pageable pageable) {
    QBomCostCache bcc = QBomCostCache.bomCostCache;
    QItem item = QItem.item;
    QItemStatus itemStatus = QItemStatus.itemStatus;

    // 데이터 조회
    List<BomCostCache> results = queryFactory
        .selectFrom(bcc)
        .join(item).on(bcc.childItemId.eq(item.itemId))   // FK 대신 ON 조건
        .join(item.itemStatus, itemStatus)
        .where(
            statusEq(request.getItemStatus()),
            nameContains(request.getItemName()),
            specificationContains(request.getSpecification())
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // count 쿼리
    JPAQuery<Long> countQuery = queryFactory
        .select(bcc.count())
        .from(bcc)
        .join(item).on(bcc.childItemId.eq(item.itemId))
        .join(item.itemStatus, itemStatus)
        .where(
            statusEq(request.getItemStatus()),
            nameContains(request.getItemName()),
            specificationContains(request.getSpecification())
        );

    return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
  }


  // 품목 구분 검색
  private BooleanExpression statusEq(final String status) {
    if (status == null || status.isEmpty()) {
      return null;
    }
    final ItemStatusStatus statusEnum = ItemStatusStatus.fromLabel(status);
    return QItem.item.itemStatus.status.eq(statusEnum);
  }

  // 품목명 검색
  private BooleanExpression nameContains(final String name) {
    return (name == null || name.isEmpty()) ? null : QItem.item.name.contains(name);
  }

  // 품목 규격 검색
  private BooleanExpression specificationContains(final String specification) {
    return (specification == null || specification.isEmpty()) ? null
        : QItem.item.specification.contains(specification);
  }


  @Override
  @Transactional(readOnly = true)
  public List<Long> findAllBomAndOrphanItemIds() {
    QBom b = QBom.bom;
    QItem i = QItem.item;

    // parent + child ID 모아서 distinct
    List<Long> bomItemIds = queryFactory
        .select(b.parentItem.itemId)
        .from(b)
        .fetch();

    bomItemIds.addAll(
        queryFactory.select(b.childItem.itemId)
            .from(b)
            .fetch()
    );

    // orphan 포함해서 전체 아이템 조회
    return queryFactory
        .selectDistinct(i.itemId)
        .from(i)
        .where(
            i.itemId.in(bomItemIds)
                .or(i.itemId.notIn(bomItemIds)) // orphan 포함
        )
        .fetch();
  }

  /**
   * 특정 rootItem 기준으로 전체 BOM 트리 구조 가져오기
   */
  public List<BomClosure> findTreeByRootItemId(final Long rootItemId) {
    QBomClosure bc = QBomClosure.bomClosure;

    return queryFactory
        .selectFrom(bc)
        .where(bc.id.ancestorItemId.eq(rootItemId))
        .orderBy(bc.depth.asc()) // 루트 → 리프 순서
        .fetch();
  }

  /**
   * rootItemId 기준 캐시 활용 조회
   */
  public List<BomCostCache> findCostCacheByRootItemId(final Long rootItemId) {
    QBomCostCache bcc = QBomCostCache.bomCostCache;

    return queryFactory
        .selectFrom(bcc)
        .where(bcc.rootItemId.eq(rootItemId))
        .orderBy(bcc.depth.asc())
        .fetch();
  }
}
