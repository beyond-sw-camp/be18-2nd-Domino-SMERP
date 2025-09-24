package com.domino.smerp.workorder.service;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.workorder.dto.request.CreateWorkOrderRequest;
import com.domino.smerp.workorder.dto.request.SearchWorkOrderRequest;
import com.domino.smerp.workorder.dto.request.UpdateWorkOrderRequest;
import com.domino.smerp.workorder.dto.response.CurrentWorkOrderListResponse;
import com.domino.smerp.workorder.dto.response.SearchWorkOrderListResponse;
import com.domino.smerp.workorder.dto.response.WorkOrderListResponse;
import com.domino.smerp.workorder.dto.response.WorkOrderResponse;
import org.springframework.data.domain.Pageable;

public interface WorkOrderService {

  //목록 조회
  WorkOrderListResponse getAllWorkOrders();

  PageResponse<SearchWorkOrderListResponse> searchWorkOrders(
      final SearchWorkOrderRequest keyword,
      final Pageable pageable);

    //상세 조회
  WorkOrderResponse getWorkOrderById(final Long id);

  CurrentWorkOrderListResponse getAllCurrentWorkOrders();

  //생성
  WorkOrderResponse createWorkOrder(final CreateWorkOrderRequest createWorkOrderRequest);

  //void createWorkOrderIfAvailable(final Long planId);

  //수정
  WorkOrderResponse updateWorkOrder(final Long id, final UpdateWorkOrderRequest updateWorkOrderRequest);

  //삭제
  WorkOrderResponse softDelete(final Long id);

  void hardDeleteWorkOrder(final Long id);
}
