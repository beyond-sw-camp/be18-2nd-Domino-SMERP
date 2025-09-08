package com.domino.smerp.purchase.itemrpocrossedtable;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRpoCrossedTableRepository extends JpaRepository<ItemRpoCrossedTable, Long> {

  // 특정 구매요청(RPO)에 속한 모든 품목 라인 조회
  List<ItemRpoCrossedTable> findByRequestPurchaseOrder_RpoId(Long rpoId);

  // 특정 RPO 안에서 특정 품목 존재 여부 확인
  Optional<ItemRpoCrossedTable> findByRequestPurchaseOrder_RpoIdAndItem_ItemId(Long rpoId,
      Long itemId);
}
