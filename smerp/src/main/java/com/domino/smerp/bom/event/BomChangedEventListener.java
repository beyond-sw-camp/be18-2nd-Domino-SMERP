package com.domino.smerp.bom.event;

import com.domino.smerp.bom.service.cache.BomCacheService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
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
  private static final ConcurrentHashMap<Long, ReentrantLock> cacheLocks = new ConcurrentHashMap<>();

  //@Async
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleBomChanged(final BomChangedEvent event) {
    final Long changedItemId = event.getChangedItemId();
    log.info("BOM 변경 이벤트 수신: changedItemId={}", changedItemId);

    // rootItemId에 해당하는 잠금 객체를 가져오거나 생성
    ReentrantLock lock = cacheLocks.computeIfAbsent(changedItemId, k -> new ReentrantLock());

    lock.lock();
    try {
      bomCacheService.invalidateAndRebuild(changedItemId);
    } finally {
      lock.unlock();
      // 작업 완료 후 락 제거
      cacheLocks.remove(changedItemId);
    }
  }
}
