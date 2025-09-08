package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderRequest;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderResponse;
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
 * 구매(PurchaseOrder) Controller
 */
@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

  private final PurchaseOrderService purchaseOrderService;

  @PostMapping
  public ResponseEntity<PurchaseOrderResponse> create(
      @RequestBody final PurchaseOrderRequest request) {
    return ResponseEntity.status(201).body(purchaseOrderService.create(request));
  }

  @GetMapping
  public ResponseEntity<List<PurchaseOrderResponse>> getAll(
      @RequestParam(defaultValue = "0") final int page,
      @RequestParam(defaultValue = "10") final int size) {
    return ResponseEntity.ok(purchaseOrderService.getAll(page, size));
  }

  @GetMapping("/{poId}")
  public ResponseEntity<PurchaseOrderResponse> getById(@PathVariable final Long poId) {
    return ResponseEntity.ok(purchaseOrderService.getById(poId));
  }

  @PatchMapping("/{poId}")
  public ResponseEntity<PurchaseOrderResponse> update(
      @PathVariable final Long poId,
      @RequestBody final PurchaseOrderRequest request) {
    return ResponseEntity.ok(purchaseOrderService.update(poId, request));
  }

  @DeleteMapping("/{poId}")
  public ResponseEntity<Void> delete(@PathVariable final Long poId) {
    purchaseOrderService.delete(poId);
    return ResponseEntity.noContent().build(); // 204
  }
}
