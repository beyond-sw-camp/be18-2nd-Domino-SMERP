package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderCreateRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderUpdateRequest;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderCreateResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderDeleteResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderGetDetailResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderGetListResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderUpdateResponse;
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
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

  private final PurchaseOrderService purchaseOrderService;

  // ✅ 구매 생성
  @PostMapping
  public ResponseEntity<PurchaseOrderCreateResponse> createPurchaseOrder(
      @RequestBody final PurchaseOrderCreateRequest request) {
    return ResponseEntity.ok(purchaseOrderService.createPurchaseOrder(request));
  }

  // ✅ 구매 목록 조회 (페이징)
  @GetMapping
  public ResponseEntity<List<PurchaseOrderGetListResponse>> getPurchaseOrders(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(purchaseOrderService.getPurchaseOrders(page, size));
  }

  // ✅ 구매 상세 조회
  @GetMapping("/{poId}")
  public ResponseEntity<PurchaseOrderGetDetailResponse> getPurchaseOrder(
      @PathVariable final Long poId) {
    return ResponseEntity.ok(purchaseOrderService.getPurchaseOrderById(poId));
  }

  // ✅ 구매 수정
  @PatchMapping("/{poId}")
  public ResponseEntity<PurchaseOrderUpdateResponse> updatePurchaseOrder(
      @PathVariable final Long poId,
      @RequestBody final PurchaseOrderUpdateRequest request) {
    return ResponseEntity.ok(purchaseOrderService.updatePurchaseOrder(poId, request));
  }

  // ✅ 구매 삭제 (소프트 삭제)
  @DeleteMapping("/{poId}")
  public ResponseEntity<PurchaseOrderDeleteResponse> deletePurchaseOrder(
      @PathVariable final Long poId) {
    return ResponseEntity.ok(purchaseOrderService.deletePurchaseOrder(poId));
  }
}
