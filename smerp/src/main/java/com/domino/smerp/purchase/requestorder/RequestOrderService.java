package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderRequest;
import com.domino.smerp.purchase.requestorder.dto.response.RequestOrderResponse;
import java.util.List;

public interface RequestOrderService {

  RequestOrderResponse create(RequestOrderRequest request);

  List<RequestOrderResponse> getAll();

  RequestOrderResponse getById(Long roId);

  RequestOrderResponse update(Long roId, RequestOrderRequest request);

  RequestOrderResponse updateStatus(Long roId, String status);

  void delete(Long roId);

  // 라인
  RequestOrderResponse.RequestOrderLineResponse addLine(Long roId,
      RequestOrderRequest.RequestOrderLineRequest request);

  List<RequestOrderResponse.RequestOrderLineResponse> getLines(Long roId);

  RequestOrderResponse.RequestOrderLineResponse updateLine(Long roId, Long lineId,
      RequestOrderRequest.RequestOrderLineRequest request);

  void deleteLine(Long roId, Long lineId);
}
