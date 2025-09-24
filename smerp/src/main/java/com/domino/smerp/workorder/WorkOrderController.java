package com.domino.smerp.workorder;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.productionplan.dto.request.SearchProductionPlanRequest;
import com.domino.smerp.productionplan.service.ProductionPlanService;
import com.domino.smerp.workorder.dto.request.CreateWorkOrderRequest;
import com.domino.smerp.workorder.dto.request.SearchWorkOrderRequest;
import com.domino.smerp.workorder.dto.request.UpdateWorkOrderRequest;
import com.domino.smerp.workorder.dto.response.CurrentWorkOrderListResponse;
import com.domino.smerp.workorder.dto.response.SearchWorkOrderListResponse;
import com.domino.smerp.workorder.dto.response.WorkOrderListResponse;
import com.domino.smerp.workorder.dto.response.WorkOrderResponse;
import com.domino.smerp.workorder.repository.WorkOrderRepository;
import com.domino.smerp.workorder.service.WorkOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/work-orders")
public class WorkOrderController {

  private final WorkOrderService workOrderService;
  private final ProductionPlanService productionPlanService;

  //작업지시 목록 조회 - soft delete 적용
  @GetMapping
  public ResponseEntity<WorkOrderListResponse> getAllProductionOrders() {
    WorkOrderListResponse workOrderListResponse = workOrderService.getAllWorkOrders();
    return ResponseEntity.status(200).body(workOrderListResponse);
  }

  //검색
  @GetMapping("/search")
  public ResponseEntity<PageResponse<SearchWorkOrderListResponse>> getLotNumbers(
      final @ModelAttribute SearchWorkOrderRequest keyword,
      final Pageable pageable) {
    return ResponseEntity.ok(workOrderService.searchWorkOrders(keyword, pageable));
  }

  //현황
  @GetMapping("/current-work-orders")
  public ResponseEntity<CurrentWorkOrderListResponse> getCurrentWorkOrders() {
    CurrentWorkOrderListResponse currentWorkOrderListResponse = workOrderService.getAllCurrentWorkOrders();
    return ResponseEntity.status(200).body(currentWorkOrderListResponse);
  }

  //상세 조회
  @GetMapping("/{work-order}")
  public ResponseEntity<WorkOrderResponse> getProductionOrderById(
      @PathVariable(name = "work-order") Long workOrderId) {
    WorkOrderResponse workOrderResponse = workOrderService.getWorkOrderById(workOrderId);
    return ResponseEntity.status(200).body(workOrderResponse);
  }

  //자동 생성

  //사용자 생성 요청
  @PostMapping
  public ResponseEntity<WorkOrderResponse> createWorkOrder(
      @Valid @RequestBody CreateWorkOrderRequest createWorkOrderRequest) {
    WorkOrderResponse workOrderResponse = workOrderService.createWorkOrder(createWorkOrderRequest);
    return ResponseEntity.status(201).body(workOrderResponse);
  }


  //수정
  @PatchMapping("/{work-order}")
  public ResponseEntity<WorkOrderResponse> updateProductionOrder(
      @PathVariable(name = "work-order") Long workOrderId,
      @Valid @RequestBody UpdateWorkOrderRequest updateWorkOrderRequest) {
    WorkOrderResponse workOrderResponse = workOrderService.updateWorkOrder(
        workOrderId, updateWorkOrderRequest);
    return ResponseEntity.status(200).body(workOrderResponse);
  }

  //삭제 - soft delete
  @DeleteMapping("/{work-order}")
  public ResponseEntity deleteProductionOrder(
      @PathVariable(name = "work-order") Long workOrderId) {
    workOrderService.softDelete(workOrderId);
    return ResponseEntity.status(204).build();
  }

}
