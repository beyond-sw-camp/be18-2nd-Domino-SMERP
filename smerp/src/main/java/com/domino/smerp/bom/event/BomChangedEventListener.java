package com.domino.smerp.bom.event;

import com.domino.smerp.bom.entity.BomClosure;
import com.domino.smerp.bom.repository.BomClosureRepository;
import com.domino.smerp.bom.service.cache.BomCacheService;
import com.domino.smerp.bom.service.command.BomCommandServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Slf4j
@Component
@RequiredArgsConstructor
public class BomChangedEventListener {

  private final BomCacheService bomCacheService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleBomChanged(final BomChangedEvent event) {
    final Long changedItemId = event.getChangedItemId();
    log.info("BOM 변경 이벤트 수신: changedItemId={}", changedItemId);

    bomCacheService.invalidateAndRebuild(changedItemId);
  }
}
