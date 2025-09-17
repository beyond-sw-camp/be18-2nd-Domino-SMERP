package com.domino.smerp.purchase.itemrequestpurchaseorder;

import com.domino.smerp.purchase.itemrequestpurchaseorder.ItemRequestPurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestPurchaseOrderRepository extends JpaRepository<ItemRequestPurchaseOrder, Long> {
    List<ItemRequestPurchaseOrder> findByRequestPurchaseOrder_RpoId(Long rpoId);
}
