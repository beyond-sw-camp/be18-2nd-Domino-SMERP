package com.domino.smerp.bom.event;

import com.domino.smerp.bom.service.cache.BomCacheService;
import com.domino.smerp.bom.service.cache.CacheRebuildWorker;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DeadlockLoserDataAccessException;
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

  private final CacheRebuildWorker worker;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleBomChanged(final BomChangedEvent event) {
    log.info("BOM 변경 이벤트 수신 → 큐에 적재: changedItemId={}", event.getChangedItemId());
    worker.enqueue(event.getChangedItemId());
  }
}
