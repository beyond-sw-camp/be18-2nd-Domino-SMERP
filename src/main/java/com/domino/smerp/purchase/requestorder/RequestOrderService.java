package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderRequest;
import com.domino.smerp.purchase.requestorder.dto.response.RequestOrderResponse;
import java.util.List;

public interface RequestOrderService {

  RequestOrderResponse create(final RequestOrderRequest request);

  List<RequestOrderResponse> getAll(final String status, final int page, final int size);

  RequestOrderResponse getById(final Long roId);

  RequestOrderResponse update(final Long roId, final RequestOrderRequest request);

  RequestOrderResponse updateStatus(final Long roId, final String status, final String reason);

  void softDelete(final Long roId);
}
