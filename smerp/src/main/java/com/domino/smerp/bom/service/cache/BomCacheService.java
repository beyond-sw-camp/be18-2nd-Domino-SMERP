package com.domino.smerp.bom.service.cache;

public interface BomCacheService {

  // TODO: 전체 캐시 재생성 시점 생각하기
  // BOM 전체 캐시 재생성
  void rebuildAllBomCache();

  // BOM 선택한 품목 캐시 재생성
  void rebuildBomCostCache(final Long rootItemId);


}
