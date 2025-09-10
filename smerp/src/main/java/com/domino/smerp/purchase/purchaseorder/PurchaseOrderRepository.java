package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.purchase.requestorder.RequestOrder;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}
