package com.domino.smerp.bom.repository.query;


import com.domino.smerp.bom.dto.response.BomRequirementResponse;
import com.domino.smerp.bom.entity.QBomCostCache;
import com.domino.smerp.item.QItem;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BomCostCacheQueryRepositoryImpl implements BomCostCacheQueryRepository {

  private final JPAQueryFactory queryFactory;

  public List<BomRequirementResponse> findResponsesByRootItemId(final Long rootItemId) {
    QBomCostCache bcc = QBomCostCache.bomCostCache;
    QItem item = QItem.item;

    return queryFactory
        .select(Projections.constructor(BomRequirementResponse.class,
            bcc.childItemId,
            item.name,
            bcc.totalQty,
            bcc.unitCost,
            bcc.totalCost,
            bcc.depth))
        .from(bcc)
        .join(item).on(item.itemId.eq(bcc.childItemId))
        .where(bcc.rootItemId.eq(rootItemId))
        .orderBy(bcc.depth.asc(), item.name.asc())
        .fetch();
  }

  public BigDecimal getTotalCost(final Long rootItemId) {
    QBomCostCache bcc = QBomCostCache.bomCostCache;

    BigDecimal result = queryFactory
        .select(Expressions.numberTemplate(BigDecimal.class, "sum({0})", bcc.totalCost))
        .from(bcc)
        .where(bcc.rootItemId.eq(rootItemId))
        .fetchOne();

    return result != null ? result : BigDecimal.ZERO;
  }

}
