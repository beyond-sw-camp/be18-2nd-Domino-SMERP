package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.dto.request.RequestPurchaseOrderRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.response.RequestPurchaseOrderResponse;
import java.util.List;

public interface RequestPurchaseOrderService {

  RequestPurchaseOrderResponse create(final RequestPurchaseOrderRequest request);

  List<RequestPurchaseOrderResponse> getAll(final String status, final int page, final int size);

  RequestPurchaseOrderResponse getById(final Long rpoId);

  RequestPurchaseOrderResponse update(final Long rpoId, final RequestPurchaseOrderRequest request);

  RequestPurchaseOrderResponse updateStatus(final Long rpoId, final String status,
      final String reason);

  void softDelete(final Long rpoId);
}
