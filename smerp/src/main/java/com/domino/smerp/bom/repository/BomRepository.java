package com.domino.smerp.bom.repository;

import com.domino.smerp.bom.entity.Bom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BomRepository extends JpaRepository<Bom, Long> {

  // 부모 품목 ID 기준 조회
  List<Bom> findByParentItem_ItemId(final Long parentItemId);

  // 부모 ID + 자식 ID 조합 존재 여부 체크
  boolean existsByParentItem_ItemIdAndChildItem_ItemId(final Long parentItemId, final Long childItemId);

  // 추가: 부모 품목 ID와 자식 품목 ID로 특정 BOM 관계 조회
  Optional<Bom> findByParentItem_ItemIdAndChildItem_ItemId(final Long parentItemId, final Long childItemId);

  // 특정 자식 품목 ID 여러 개 삭제
  @Modifying
  @Query("DELETE FROM Bom b WHERE b.childItem.itemId IN :childItemIds")
  void deleteAllByChildItem_ItemIdIn(@Param("childItemIds") final List<Long> childItemIds);
}
