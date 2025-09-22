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
  public Page<BomClosure> searchBoms(final Long itemId, final Pageable pageable) {
    QBomClosure bc = QBomClosure.bomClosure;

    // 해당 품목이 ancestor든 descendant든 포함
    List<BomClosure> results = queryFactory
        .selectFrom(bc)
        .where(
            bc.id.ancestorItemId.eq(itemId)
              .or(bc.id.descendantItemId.eq(itemId))
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    JPAQuery<Long> countQuery = queryFactory
        .select(bc.count())
        .from(bc)
        .where(
            bc.id.ancestorItemId.eq(itemId)
                .or(bc.id.descendantItemId.eq(itemId))
        );

    return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
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
