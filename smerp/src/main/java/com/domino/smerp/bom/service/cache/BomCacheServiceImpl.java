package com.domino.smerp.bom.service.cache;

import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.repository.BomCostCacheRepository;
import com.domino.smerp.bom.repository.BomRepository;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BomCacheServiceImpl implements BomCacheService {

  private final ItemService itemService;

  private final BomRepository bomRepository;
  private final BomCostCacheRepository bomCostCacheRepository;
  private final BomCacheBuilder bomCacheBuilder;


  // BOM 캐시 무효화 후 재계산
  @Override
  @Transactional
  public void invalidateAndRebuild(final Long rootItemId) {
    bomCostCacheRepository.deleteByRootItemId(rootItemId);
    rebuildBomCostCache(rootItemId);
  }

  // BOM 캐시 조회
  @Override
  @Transactional(readOnly = true)
  public List<BomCostCache> getCacheByRootItemId(final Long rootItemId) {
    return bomCostCacheRepository.findByRootItemId(rootItemId);
  }

  // BOM 전체 캐시 재생성
  @Override
  @Transactional
  public void rebuildAllBomCache() {
    bomCostCacheRepository.deleteAll();
    final List<Long> allItemIds = bomRepository.findAllBomAndOrphanItemIds(); // 루트 + 고아까지 포함되게 변경
    log.info("전체 ItemIds (루트+고아) = {}", allItemIds);
    for (final Long rootItemId : allItemIds) {
      rebuildBomCostCache(rootItemId);
    }
  }


  // BOM 선택한 품목 캐시 재생성 (Listener에서 호출됨)
  @Override
  @Transactional
  public void rebuildBomCostCache(final Long rootItemId) {
    bomCostCacheRepository.deleteByRootItemId(rootItemId);
    final Item rootItem = itemService.findItemById(rootItemId);

    final List<BomCostCache> caches = bomCacheBuilder.build(rootItem);
    bomCostCacheRepository.saveAll(caches);
  }


}
