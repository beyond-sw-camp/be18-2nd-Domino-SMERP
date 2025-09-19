package com.domino.smerp.bom.service.cache;

import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.repository.BomCostCacheRepository;
import com.domino.smerp.bom.service.command.BomCommandServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BomCacheServiceImpl implements BomCacheService {

  private final BomCostCacheRepository cacheRepository;
  private final BomCommandServiceImpl bomCommandService;


  // BOM 캐시 무효화 후 재계산
  @Override
  @Transactional
  public void invalidateAndRebuild(final Long rootItemId) {
    cacheRepository.deleteByRootItemId(rootItemId);
    bomCommandService.rebuildBomCostCache(rootItemId);
  }


  // BOM 캐시 단순 무효화
  @Override
  @Transactional
  public void invalidateOnly(final Long rootItemId) {
    cacheRepository.deleteByRootItemId(rootItemId);
  }

  // BOM 캐시 조회
  @Override
  @Transactional(readOnly = true)
  public List<BomCostCache> getCacheByRootItemId(final Long rootItemId) {
    return cacheRepository.findByRootItemId(rootItemId);
  }
}
