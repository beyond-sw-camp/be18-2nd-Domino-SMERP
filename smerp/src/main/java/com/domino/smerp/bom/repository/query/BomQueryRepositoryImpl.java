package com.domino.smerp.bom.repository.query;

import com.domino.smerp.bom.entity.BomClosure;
import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.entity.QBomClosure;
import com.domino.smerp.bom.entity.QBomCostCache;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BomQueryRepositoryImpl implements BomQueryRepository {

  private final JPAQueryFactory queryFactory;

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
