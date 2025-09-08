package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderRequest;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderResponse;
import java.util.List;

public interface PurchaseOrderService {

  PurchaseOrderResponse create(final PurchaseOrderRequest request);

  List<PurchaseOrderResponse> getAll(final int page, final int size);

  PurchaseOrderResponse getById(final Long poId);

  PurchaseOrderResponse update(final Long poId, final PurchaseOrderRequest request);

  void delete(final Long poId);
}
