package com.domino.smerp.bom.event;

import com.domino.smerp.bom.repository.BomCostCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
public class BomCacheInvalidator {

  private final BomCostCacheRepository cacheRepository;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onBomChanged(final BomChangedEvent event) {
    cacheRepository.deleteByRootItemId(event.getChangedItemId());
  }
}
