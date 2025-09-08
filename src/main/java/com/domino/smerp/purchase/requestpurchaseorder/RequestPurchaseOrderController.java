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

  // ===== 헤더 =====

  @PostMapping
  public ResponseEntity<RequestPurchaseOrderResponse> create(
      @RequestBody final RequestPurchaseOrderRequest request) {
    return ResponseEntity.status(201).body(requestPurchaseOrderService.create(request));
  }

  @GetMapping
  public ResponseEntity<List<RequestPurchaseOrderResponse>> getAll() {
    return ResponseEntity.ok(requestPurchaseOrderService.getAll());
  }

  @GetMapping("/{rpoId}")
  public ResponseEntity<RequestPurchaseOrderResponse> getById(@PathVariable final Long rpoId) {
    return ResponseEntity.ok(requestPurchaseOrderService.getById(rpoId));
  }

  @PatchMapping("/{rpoId}")
  public ResponseEntity<RequestPurchaseOrderResponse> update(
      @PathVariable final Long rpoId,
      @RequestBody final RequestPurchaseOrderRequest request) {
    return ResponseEntity.ok(requestPurchaseOrderService.update(rpoId, request));
  }

  @PatchMapping("/{rpoId}/status")
  public ResponseEntity<RequestPurchaseOrderResponse> updateStatus(
      @PathVariable final Long rpoId,
      @RequestParam final String status,
      @RequestParam(required = false) final String reason) {
    return ResponseEntity.ok(requestPurchaseOrderService.updateStatus(rpoId, status, reason));
  }

  @DeleteMapping("/{rpoId}")
  public ResponseEntity<Void> softDelete(@PathVariable final Long rpoId) {
    requestPurchaseOrderService.softDelete(rpoId);
    return ResponseEntity.noContent().build(); // 204
  }

  // ===== 라인 =====

  @PostMapping("/{rpoId}/items")
  public ResponseEntity<RequestPurchaseOrderResponse.RequestPurchaseOrderLineResponse> addLine(
      @PathVariable final Long rpoId,
      @RequestBody final RequestPurchaseOrderRequest.RequestPurchaseOrderLineRequest request) {
    return ResponseEntity.status(201).body(requestPurchaseOrderService.addLine(rpoId, request));
  }

  @GetMapping("/{rpoId}/items")
  public ResponseEntity<List<RequestPurchaseOrderResponse.RequestPurchaseOrderLineResponse>> getLines(
      @PathVariable final Long rpoId) {
    return ResponseEntity.ok(requestPurchaseOrderService.getLinesByRpoId(rpoId));
  }

  @PatchMapping("/{rpoId}/items/{lineId}")
  public ResponseEntity<RequestPurchaseOrderResponse.RequestPurchaseOrderLineResponse> updateLine(
      @PathVariable final Long rpoId,
      @PathVariable final Long lineId,
      @RequestBody final RequestPurchaseOrderRequest.RequestPurchaseOrderLineRequest request) {
    return ResponseEntity.ok(requestPurchaseOrderService.updateLine(rpoId, lineId, request));
  }

  @DeleteMapping("/{rpoId}/items/{lineId}")
  public ResponseEntity<Void> deleteLine(
      @PathVariable final Long rpoId,
      @PathVariable final Long lineId) {
    requestPurchaseOrderService.deleteLine(rpoId, lineId);
    return ResponseEntity.noContent().build();
  }
}
