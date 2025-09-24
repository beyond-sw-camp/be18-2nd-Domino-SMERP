package com.domino.smerp.purchase.requestpurchaseorder.repository.query;

import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.SearchRequestPurchaseOrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RequestPurchaseOrderQueryRepository {
    Page<RequestPurchaseOrder> searchRequestPurchaseOrder(SearchRequestPurchaseOrderRequest condition, Pageable pageable);
}
