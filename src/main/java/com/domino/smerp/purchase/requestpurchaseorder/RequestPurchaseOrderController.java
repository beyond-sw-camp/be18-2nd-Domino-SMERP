package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.dto.request.RequestPurchaseOrderRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.response.RequestPurchaseOrderResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 구매요청(RequestPurchaseOrder) Controller
 */
@RestController
@RequestMapping("/api/request-purchase-orders")
@RequiredArgsConstructor
public class RequestPurchaseOrderController {

  private final RequestPurchaseOrderService requestPurchaseOrderService;

  /**
   * 구매요청 등록
   */
  @PostMapping
  public ResponseEntity<RequestPurchaseOrderResponse> createRequestPurchaseOrder(
      @RequestBody final RequestPurchaseOrderRequest request) {
    RequestPurchaseOrderResponse response = requestPurchaseOrderService.create(request);
    return ResponseEntity.status(201).body(response);
  }

  /**
   * 구매요청 목록 조회 (페이징)
   */
  @GetMapping
  public ResponseEntity<List<RequestPurchaseOrderResponse>> getRequestPurchaseOrders(
      @RequestParam(required = false) final String status,
      @RequestParam(defaultValue = "0") final int page,
      @RequestParam(defaultValue = "10") final int size) {
    List<RequestPurchaseOrderResponse> responses =
        requestPurchaseOrderService.getAll(status, page, size);
    return ResponseEntity.ok(responses);
  }

  /**
   * 구매요청 상세 조회
   */
  @GetMapping("/{rpoId}")
  public ResponseEntity<RequestPurchaseOrderResponse> getRequestPurchaseOrder(
      @PathVariable final Long rpoId) {
    RequestPurchaseOrderResponse response = requestPurchaseOrderService.getById(rpoId);
    return ResponseEntity.ok(response);
  }

  /**
   * 구매요청 수정
   */
  @PatchMapping("/{rpoId}")
  public ResponseEntity<RequestPurchaseOrderResponse> updateRequestPurchaseOrder(
      @PathVariable final Long rpoId,
      @RequestBody final RequestPurchaseOrderRequest request) {
    RequestPurchaseOrderResponse response = requestPurchaseOrderService.update(rpoId, request);
    return ResponseEntity.ok(response);
  }

  /**
   * 구매요청 상태 변경
   */
  @PatchMapping("/{rpoId}/status")
  public ResponseEntity<RequestPurchaseOrderResponse> updateStatus(
      @PathVariable final Long rpoId,
      @RequestParam final String status,
      @RequestParam(required = false) final String reason) {
    RequestPurchaseOrderResponse response = requestPurchaseOrderService.updateStatus(rpoId, status,
        reason);
    return ResponseEntity.ok(response);
  }

  /**
   * 구매요청 소프트 삭제
   */
  @DeleteMapping("/{rpoId}")
  public ResponseEntity<Void> deleteRequestPurchaseOrder(@PathVariable final Long rpoId) {
    requestPurchaseOrderService.softDelete(rpoId);
    return ResponseEntity.noContent().build(); // 204
  }
}
