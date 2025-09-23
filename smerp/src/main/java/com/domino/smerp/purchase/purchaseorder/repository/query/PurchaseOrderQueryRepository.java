package com.domino.smerp.purchase.purchaseorder.repository.query;

import com.domino.smerp.purchase.purchaseorder.PurchaseOrder;
import com.domino.smerp.purchase.purchaseorder.dto.request.SearchPurchaseOrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PurchaseOrderQueryRepository {
    Page<PurchaseOrder> searchPurchaseOrder(final SearchPurchaseOrderRequest condition, final Pageable pageable);

}
