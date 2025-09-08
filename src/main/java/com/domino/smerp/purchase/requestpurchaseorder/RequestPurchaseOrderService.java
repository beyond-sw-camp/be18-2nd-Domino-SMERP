package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.dto.request.RequestPurchaseOrderRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.response.RequestPurchaseOrderResponse;
import java.util.List;

public interface RequestPurchaseOrderService {

  // 헤더
  RequestPurchaseOrderResponse create(final RequestPurchaseOrderRequest request);

  List<RequestPurchaseOrderResponse> getAll();

  RequestPurchaseOrderResponse getById(final Long rpoId);

  RequestPurchaseOrderResponse update(final Long rpoId, final RequestPurchaseOrderRequest request);

  RequestPurchaseOrderResponse updateStatus(final Long rpoId, final String status,
      final String reason);

  void softDelete(final Long rpoId);

  // 라인
  RequestPurchaseOrderResponse.RequestPurchaseOrderLineResponse addLine(final Long rpoId,
      final RequestPurchaseOrderRequest.RequestPurchaseOrderLineRequest request);

  List<RequestPurchaseOrderResponse.RequestPurchaseOrderLineResponse> getLinesByRpoId(
      final Long rpoId);

  RequestPurchaseOrderResponse.RequestPurchaseOrderLineResponse updateLine(final Long rpoId,
      final Long lineId, final RequestPurchaseOrderRequest.RequestPurchaseOrderLineRequest request);

  void deleteLine(final Long rpoId, final Long lineId);
}
