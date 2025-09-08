package com.domino.smerp.purchase.purchaseorder;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

  // 발주 전표 ID로 조회
  PurchaseOrder findByRequestOrder_RoId(Long roId);

  // 구매 전표 ID로 조회
  PurchaseOrder findByPoId(Long poId);

  // 생성일자 범위 조회
  List<PurchaseOrder> findByCreatedDateBetween(Instant start, Instant end);

  // 수정일자 범위 조회
  List<PurchaseOrder> findByUpdatedDateBetween(Instant start, Instant end);

  // 사용자 기준 조회
  List<PurchaseOrder> findByRequestOrder_User_UserId(Long userId);

  // 거래처 기준 조회
  List<PurchaseOrder> findByRequestOrder_Client_ClientId(Long clientId);

  // 품목 기준 조회 (교차테이블 통해)
  List<PurchaseOrder> findByRequestOrder_Items_Item_ItemId(Long itemId);

  // 창고 기준 조회 (예: 품목이 창고를 참조한다고 가정)
  List<PurchaseOrder> findByRequestOrder_Items_Item_Warehouse_WarehouseId(Long warehouseId);

  // 수량 기준 조회
  List<PurchaseOrder> findByQty(int qty);

  // 비고 기준 조회
  List<PurchaseOrder> findByRemarkContaining(String keyword);
}
