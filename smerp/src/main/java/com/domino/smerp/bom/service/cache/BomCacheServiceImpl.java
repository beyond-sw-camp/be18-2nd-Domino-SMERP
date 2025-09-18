package com.domino.smerp.bom.service.cache;

import com.domino.smerp.bom.repository.BomCostCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BomCacheServiceImpl implements BomCacheService {

  private final BomCostCacheRepository cacheRepository;


  @Override
  @Transactional
  public void invalidateAndRebuild(final Long rootItemId) {
    cacheRepository.deleteByRootItemId(rootItemId);
    // TODO: 재계산 로직 호출 (예: BomCommandServiceImpl.rebuildBomCostCache)
  }
}
