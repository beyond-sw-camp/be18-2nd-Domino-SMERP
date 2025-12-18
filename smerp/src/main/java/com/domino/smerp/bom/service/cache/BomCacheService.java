package com.domino.smerp.bom.service.cache;

import com.domino.smerp.bom.entity.BomCostCache;
import java.util.List;

public interface BomCacheService {

  List<BomCostCache> getCacheByRootItemId(final Long rootItemId);


  // TODO: 전체 캐시 재생성 시점 생각하기
  // BUG: 캐시 재생성 시 `bom_closure`, `bom_cost_cache` 서로 row수 다름
  // BOM 전체 캐시 재생성
  void rebuildAllBomCache();

  // BOM 선택한 품목 캐시 재생성
  void rebuildBomCostCache(final Long rootItemId);


}
