package com.domino.smerp.bom.repository;

import com.domino.smerp.bom.entity.BomClosure;
import com.domino.smerp.bom.entity.BomClosureId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BomClosureRepository extends JpaRepository<BomClosure, BomClosureId> {


  // command
  @Modifying
  @Query(value = """
      INSERT INTO bom_closure (ancestor_item_id, descendant_item_id, depth)
      VALUES (:ancestorId, :descendantId, :depth)
      ON DUPLICATE KEY UPDATE depth = VALUES(depth)
      """, nativeQuery = true)
  void upsertBomClosure(@Param("ancestorId") Long ancestorId,
      @Param("descendantId") Long descendantId,
      @Param("depth") Integer depth);

  // 특정 품목을 조상으로 하는 모든 관계 삭제 (해당 품목의 모든 BOM 트리 삭제 시 사용)
  @Modifying
  @Query("DELETE FROM BomClosure bc WHERE bc.id.ancestorItemId = :ancestorItemId")
  void deleteByAncestorItemId(final @Param("ancestorItemId") Long ancestorItemId);

  // 특정 품목을 자손으로 하는 모든 관계 삭제 (품목 자체가 삭제될 때 사용)
  @Modifying
  @Query("DELETE FROM BomClosure bc WHERE bc.id.descendantItemId = :descendantItemId")
  void deleteByDescendantItemId(final @Param("descendantItemId") Long descendantItemId);


  // ============================================================================
  // query
  // 추가: 특정 자손 품목을 조상으로 하는 모든 관계 조회
  List<BomClosure> findById_AncestorItemId(final Long ancestorItemId);

  // 추가: 특정 자손 품목을 기준으로 모든 조상 관계 조회
  List<BomClosure> findById_DescendantItemId(final Long descendantItemId);

  // 특정 조상-자손 관계가 존재하는지 확인
  boolean existsById_AncestorItemIdAndId_DescendantItemId(Long ancestorId, Long descendantId);



}