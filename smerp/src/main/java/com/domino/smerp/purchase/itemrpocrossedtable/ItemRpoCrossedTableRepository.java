package com.domino.smerp.purchase.itemrpocrossedtable;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRpoCrossedTableRepository extends JpaRepository<ItemRpoCrossedTable, Long> {

  // 구매요청 ID 기준으로 교차테이블 조회
  List<ItemRpoCrossedTable> findByRequestPurchaseOrderRpoId(Long rpoId);

  // 품목 ID 기준 조회
  List<ItemRpoCrossedTable> findByItemItemId(Long itemId);

  // 수량 기준 조회
  List<ItemRpoCrossedTable> findByQty(int qty);
}
