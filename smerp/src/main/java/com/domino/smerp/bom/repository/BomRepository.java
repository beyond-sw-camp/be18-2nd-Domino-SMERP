package com.domino.smerp.bom.repository;

import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.bom.repository.query.BomQueryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BomRepository extends JpaRepository<Bom, Long> , BomQueryRepository {

  // 모든 BOM 루트 품목 ID 조회
  // 다른 BOM의 자식 품목이 아닌 모든 품목의 ID를 찾습니다.
  @Query("SELECT DISTINCT b.parentItem.itemId FROM Bom b WHERE b.parentItem.itemId NOT IN (SELECT bc.childItem.itemId FROM Bom bc)")
  List<Long> findAllRootItemIds();

  // 부모 품목 ID 기준 조회
  List<Bom> findByParentItem_ItemId(final Long parentItemId);

  // BOM 품목구분 ID로 BOM 관계 조회
  List<Bom> findByChildItem_ItemStatus_ItemStatusId(final Long itemStatusId);

  // 부모 ID + 자식 ID 조합 존재 여부 체크
  boolean existsByParentItem_ItemIdAndChildItem_ItemId(final Long parentItemId, final Long childItemId);

  // 추가: 부모 품목 ID와 자식 품목 ID로 특정 BOM 관계 조회
  Optional<Bom> findByParentItem_ItemIdAndChildItem_ItemId(final Long parentItemId, final Long childItemId);

  // bom삭제
  boolean existsByParentItem_ItemId(final Long childItemId);


  // 특정 자식 품목 ID 여러 개 삭제
  @Modifying
  @Query("DELETE FROM Bom b WHERE b.childItem.itemId IN :childItemIds")
  void deleteAllByChildItem_ItemIdIn(@Param("childItemIds") final List<Long> childItemIds);
}
