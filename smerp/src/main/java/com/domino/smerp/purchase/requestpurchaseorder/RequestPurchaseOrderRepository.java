package com.domino.smerp.purchase.requestpurchaseorder;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestPurchaseOrderRepository extends JpaRepository<RequestPurchaseOrder, Long> {

  // created_date 조회
  List<RequestPurchaseOrder> findByCreatedDateBetween(Instant start, Instant end);

  // updated_date 조회
  List<RequestPurchaseOrder> findByUpdatedDateBetween(Instant start, Instant end);

  // user_id 조회
  List<RequestPurchaseOrder> findByUser_UserId(Long userId);

  // item_id 조회 (교차테이블 기준)
  List<RequestPurchaseOrder> findByItemRpoCrossedTablesItemItemId(Long itemId);

  // qty 조회 (교차테이블 기준)
  List<RequestPurchaseOrder> findByItemRpoCrossedTablesQty(int qty);

  // remark 조회 (LIKE 검색)
  List<RequestPurchaseOrder> findByRemarkContaining(String keyword);
}
