package com.domino.smerp.purchase.purchaseorder.repository.query;

import com.domino.smerp.purchase.purchaseorder.PurchaseOrder;
import com.domino.smerp.purchase.purchaseorder.dto.request.SearchPurchaseOrderRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.SearchSummaryPurchaseOrderRequest;
import com.domino.smerp.purchase.purchaseorder.dto.response.SummaryPurchaseOrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PurchaseOrderQueryRepository {
    Page<PurchaseOrder> searchPurchaseOrder(final SearchPurchaseOrderRequest condition, final Pageable pageable);

    List<SummaryPurchaseOrderResponse> searchSummaryPurchaseOrders(SearchSummaryPurchaseOrderRequest condition, Pageable pageable);
}
