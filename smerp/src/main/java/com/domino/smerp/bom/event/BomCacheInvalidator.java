package com.domino.smerp.bom.event;

import com.domino.smerp.bom.repository.BomCostCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BomCacheInvalidator {

  private final BomCostCacheRepository cacheRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW) // 새로운 트랜잭션 시작하도록 설정
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onBomChanged(final BomChangedEvent event) {
    cacheRepository.deleteByRootItemId(event.getChangedItemId());
  }
}
