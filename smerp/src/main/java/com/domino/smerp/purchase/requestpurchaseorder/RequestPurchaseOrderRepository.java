package com.domino.smerp.purchase.requestpurchaseorder;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestPurchaseOrderRepository
    extends JpaRepository<RequestPurchaseOrder, Long> {

  Page<RequestPurchaseOrder> findByUserId(Long userId, Pageable pageable);

  Page<RequestPurchaseOrder> findByStatus(
      com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrderStatus status,
      Pageable pageable);

  Page<RequestPurchaseOrder> findByUserIdAndStatus(Long userId,
      com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrderStatus status,
      Pageable pageable);
}
