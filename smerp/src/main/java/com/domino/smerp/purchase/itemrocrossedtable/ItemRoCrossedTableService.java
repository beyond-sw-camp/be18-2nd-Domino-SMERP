package com.domino.smerp.purchase.itemrocrossedtable;

import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderRequest;
import com.domino.smerp.purchase.requestorder.dto.response.RequestOrderResponse;
import java.util.List;

/**
 * 발주 품목 교차테이블 Service
 */
public interface ItemRoCrossedTableService {

  RequestOrderResponse.RequestOrderLineResponse addLine(final Long roId,
      final RequestOrderRequest.RequestOrderLineRequest request);

  List<RequestOrderResponse.RequestOrderLineResponse> getLinesByRoId(final Long roId);

  RequestOrderResponse.RequestOrderLineResponse updateLine(final Long roId, final Long lineId,
      final RequestOrderRequest.RequestOrderLineRequest request);

  void deleteLine(final Long roId, final Long lineId);
}
