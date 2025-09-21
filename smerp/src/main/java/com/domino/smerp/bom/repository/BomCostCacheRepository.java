package com.domino.smerp.bom.repository;

import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.repository.cache.BomCostCacheQueryRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BomCostCacheRepository extends JpaRepository<BomCostCache, Long> ,BomCostCacheQueryRepository{

  // 특정 최상위 품목의 모든 캐시 조회
  List<BomCostCache> findByRootItemId(final Long rootItemId);

  // 특정 최상위 품목의 모든 캐시 삭제
  @Modifying
  @Query("DELETE FROM BomCostCache bcc WHERE bcc.rootItemId = :rootItemId")
  void deleteByRootItemId(final @Param("rootItemId") Long rootItemId);
}
