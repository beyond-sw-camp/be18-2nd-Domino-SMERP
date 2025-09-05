package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.constants.RequestPurchaseOrderStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestPurchaseOrderRepository extends JpaRepository<RequestPurchaseOrder, Long> {

  // 상태별 조회
  List<RequestPurchaseOrder> findByStatus(RequestPurchaseOrderStatus status);

  // 특정 사용자 기준 조회
  List<RequestPurchaseOrder> findByUser_UserId(Long userId);
}
