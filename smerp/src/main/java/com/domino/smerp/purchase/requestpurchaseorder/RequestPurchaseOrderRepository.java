package com.domino.smerp.purchase.requestpurchaseorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestPurchaseOrderRepository extends JpaRepository<RequestPurchaseOrder, Long> {
}
