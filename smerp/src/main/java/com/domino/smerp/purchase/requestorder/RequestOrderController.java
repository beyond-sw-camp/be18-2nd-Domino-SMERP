package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderRequest;
import com.domino.smerp.purchase.requestorder.dto.response.RequestOrderResponse;
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

@RestController
@RequestMapping("/api/request-orders")
@RequiredArgsConstructor
public class RequestOrderController {

  private final RequestOrderService requestOrderService;

  // ===== 헤더 =====

  @PostMapping
  public ResponseEntity<RequestOrderResponse> create(
      @RequestBody final RequestOrderRequest request) {
    return ResponseEntity.status(201).body(requestOrderService.create(request));
  }

  @GetMapping
  public ResponseEntity<List<RequestOrderResponse>> getAll() {
    return ResponseEntity.ok(requestOrderService.getAll());
  }

  @GetMapping("/{roId}")
  public ResponseEntity<RequestOrderResponse> getById(
      @PathVariable final Long roId) {
    return ResponseEntity.ok(requestOrderService.getById(roId));
  }

  @PatchMapping("/{roId}")
  public ResponseEntity<RequestOrderResponse> update(
      @PathVariable final Long roId,
      @RequestBody final RequestOrderRequest request) {
    return ResponseEntity.ok(requestOrderService.update(roId, request));
  }

  @PatchMapping("/{roId}/status")
  public ResponseEntity<RequestOrderResponse> updateStatus(
      @PathVariable final Long roId,
      @RequestParam final String status) {
    return ResponseEntity.ok(requestOrderService.updateStatus(roId, status));
  }

  @DeleteMapping("/{roId}")
  public ResponseEntity<Void> delete(
      @PathVariable final Long roId) {
    requestOrderService.delete(roId);
    return ResponseEntity.noContent().build();
  }

  // ===== 라인 =====

  @PostMapping("/{roId}/items")
  public ResponseEntity<RequestOrderResponse.RequestOrderLineResponse> addLine(
      @PathVariable final Long roId,
      @RequestBody final RequestOrderRequest.RequestOrderLineRequest request) {
    return ResponseEntity.status(201).body(requestOrderService.addLine(roId, request));
  }

  @GetMapping("/{roId}/items")
  public ResponseEntity<List<RequestOrderResponse.RequestOrderLineResponse>> getLines(
      @PathVariable final Long roId) {
    return ResponseEntity.ok(requestOrderService.getLines(roId));
  }

  @PatchMapping("/{roId}/items/{lineId}")
  public ResponseEntity<RequestOrderResponse.RequestOrderLineResponse> updateLine(
      @PathVariable final Long roId,
      @PathVariable final Long lineId,
      @RequestBody final RequestOrderRequest.RequestOrderLineRequest request) {
    return ResponseEntity.ok(requestOrderService.updateLine(roId, lineId, request));
  }

  @DeleteMapping("/{roId}/items/{lineId}")
  public ResponseEntity<Void> deleteLine(
      @PathVariable final Long roId,
      @PathVariable final Long lineId) {
    requestOrderService.deleteLine(roId, lineId);
    return ResponseEntity.noContent().build();
  }
}
