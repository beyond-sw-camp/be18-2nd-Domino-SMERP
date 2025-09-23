package com.domino.smerp.workorder.service;

import com.domino.smerp.workorder.dto.request.CreateWorkOrderRequest;
import com.domino.smerp.workorder.dto.request.UpdateWorkOrderRequest;
import com.domino.smerp.workorder.dto.response.CurrentWorkOrderListResponse;
import com.domino.smerp.workorder.dto.response.WorkOrderListResponse;
import com.domino.smerp.workorder.dto.response.WorkOrderResponse;

public interface WorkOrderService {

  //목록 조회
  WorkOrderListResponse getAllWorkOrders();

  CurrentWorkOrderListResponse getAllCurrentWorkOrders();


    //상세 조회
  WorkOrderResponse getWorkOrderById(final Long id);

  //생성
  WorkOrderResponse createWorkOrder(final CreateWorkOrderRequest createWorkOrderRequest);

  //void createWorkOrderIfAvailable(final Long planId);

  //수정
  WorkOrderResponse updateWorkOrder(final Long id, final UpdateWorkOrderRequest updateWorkOrderRequest);

  //삭제
  WorkOrderResponse softDelete(final Long id);

  void hardDeleteWorkOrder(final Long id);
}
