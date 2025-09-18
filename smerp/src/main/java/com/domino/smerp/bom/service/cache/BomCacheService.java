package com.domino.smerp.bom.service.cache;

public interface BomCacheService {

  void invalidateAndRebuild(final Long rootItemId);

}
