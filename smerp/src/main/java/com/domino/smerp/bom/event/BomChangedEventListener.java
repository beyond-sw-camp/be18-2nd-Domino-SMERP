package com.domino.smerp.bom.event;

import com.domino.smerp.bom.service.cache.BomCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BomChangedEventListener {

  private final BomCacheService bomCacheService;

  @Async
  @Transactional(propagation = Propagation.REQUIRES_NEW) // 새로운 트랜잭션 시작하도록 설정
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleBomChanged(final BomChangedEvent event) {
    final Long changedItemId = event.getChangedItemId();
    log.info("BOM 변경 이벤트 수신: changedItemId={}", changedItemId);

    bomCacheService.invalidateAndRebuild(changedItemId);
  }
}
