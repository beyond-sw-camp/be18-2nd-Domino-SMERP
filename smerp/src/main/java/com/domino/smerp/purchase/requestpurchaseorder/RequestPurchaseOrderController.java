package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.dto.RequestPurchaseOrderRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.RequestPurchaseOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rpos")
public class RequestPurchaseOrderController {

  private final RequestPurchaseOrderService service;

  @PostMapping
  public ResponseEntity<RequestPurchaseOrderResponse> create(
      @Valid @RequestBody RequestPurchaseOrderRequest req) {
    return ResponseEntity.ok(RequestPurchaseOrderResponse.from(service.create(req)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<RequestPurchaseOrderResponse> get(@PathVariable Long id) {
    return ResponseEntity.ok(RequestPurchaseOrderResponse.from(service.get(id)));
  }

  @GetMapping
  public ResponseEntity<Page<RequestPurchaseOrderResponse>> list(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(required = false) Long userId,
      @RequestParam(required = false) RequestPurchaseOrderStatus status
  ) {
    PageRequest pageable = PageRequest.of(page, size);
    Page<RequestPurchaseOrderResponse> body = service.list(pageable, userId, status)
        .map(RequestPurchaseOrderResponse::from);
    return ResponseEntity.ok(body);
  }

  @PatchMapping("/{id}/delivery-date")
  public ResponseEntity<RequestPurchaseOrderResponse> changeDeliveryDate(
      @PathVariable Long id,
      @RequestBody Map<String, String> body
  ) {
    LocalDate date = LocalDate.parse(body.get("deliveryDate"));
    return ResponseEntity.ok(
        RequestPurchaseOrderResponse.from(service.changeDeliveryDate(id, date)));
  }

  @PatchMapping("/{id}/status")
  public ResponseEntity<RequestPurchaseOrderResponse> changeStatus(
      @PathVariable Long id,
      @RequestBody Map<String, String> body
  ) {
    RequestPurchaseOrderStatus status = RequestPurchaseOrderStatus.valueOf(
        body.get("status").toUpperCase());
    String reason = body.getOrDefault("reason", null);
    return ResponseEntity.ok(
        RequestPurchaseOrderResponse.from(service.changeStatus(id, status, reason)));
  }

  @PatchMapping("/{id}/remark")
  public ResponseEntity<RequestPurchaseOrderResponse> changeRemark(
      @PathVariable Long id,
      @RequestBody Map<String, String> body
  ) {
    return ResponseEntity.ok(
        RequestPurchaseOrderResponse.from(service.changeRemark(id, body.get("remark"))));
  }

  /**
   * ✅ 전표일자 변경 PATCH
   */
  @PatchMapping("/{id}/document-date")
  public ResponseEntity<RequestPurchaseOrderResponse> changeDocumentDate(
      @PathVariable Long id,
      @RequestBody Map<String, String> body
  ) {
    LocalDate date = LocalDate.parse(body.get("updatedDate"));
    return ResponseEntity.ok(
        RequestPurchaseOrderResponse.from(service.changeDocumentDate(id, date)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
