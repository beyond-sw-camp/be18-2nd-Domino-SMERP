package com.domino.smerp.bom.service.cache;

import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.repository.BomCostCacheRepository;
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

  private final BomCostCacheRepository bomCostCacheRepository;
  private final BomCacheBuilder bomCacheBuilder;

  // BOM 전체 캐시 재생성
  @Override
  @Transactional
  public void rebuildAllBomCache() {
    bomCostCacheRepository.deleteAll();
    final List<Long> allItemIds = itemService.findAllActiveItemIds();
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
