package com.domino.smerp.bom.service.cache;

import com.domino.smerp.bom.entity.BomCostCache;
import java.util.List;

public interface BomCacheService {

  void invalidateAndRebuild(final Long rootItemId);

  List<BomCostCache> getCacheByRootItemId(final Long rootItemId);

  // BOM 전체 캐시 재생성
  void rebuildAllBomCache();

  // BOM 선택한 품목 캐시 재생성
  void rebuildBomCostCache(final Long rootItemId);


}
