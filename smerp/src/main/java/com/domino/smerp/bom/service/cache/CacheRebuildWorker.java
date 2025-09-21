package com.domino.smerp.bom.service.cache;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CacheRebuildWorker {
  private final BomCacheService bomCacheService;
  private final BlockingQueue<Long> queue = new LinkedBlockingQueue<>();

  @Async("cacheExecutor") // 전용 스레드 풀에서 실행
  public void startWorker() {
    while (true) {
      try {
        Long rootId = queue.take(); // 큐에서 하나씩 꺼냄 (blocking)
        log.info("캐시 리빌드 시작: rootId={}", rootId);
        bomCacheService.invalidateAndRebuild(rootId);
      } catch (Exception e) {
        log.error("캐시 리빌드 실패", e);
      }
    }
  }

  public void enqueue(Long rootId) {
    queue.offer(rootId);
    log.info("캐시 리빌드 요청 큐에 적재: rootId={}", rootId);
  }

}
