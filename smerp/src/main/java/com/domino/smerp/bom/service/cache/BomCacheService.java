package com.domino.smerp.bom.service.cache;

import com.domino.smerp.bom.entity.BomCostCache;
import java.util.List;

public interface BomCacheService {

  void invalidateAndRebuild(final Long rootItemId);

  void invalidateOnly(final Long rootItemId);

  List<BomCostCache> getCacheByRootItemId(final Long rootItemId);
}
