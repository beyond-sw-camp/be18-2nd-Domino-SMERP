package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderCreateRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderUpdateRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.SearchPurchaseOrderRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.SearchSummaryPurchaseOrderRequest;
import com.domino.smerp.purchase.purchaseorder.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
  public ResponseEntity<PageResponse<PurchaseOrderGetListResponse>> searchPurchaseOrdes(
          @ModelAttribute SearchPurchaseOrderRequest keyword, Pageable pageable) {
    return ResponseEntity.ok(purchaseOrderService.searchPurchaseOrdes(keyword, pageable));
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

  // ✅ 구매 현황 조회
    @GetMapping("/summary")
    public ResponseEntity<List<SummaryPurchaseOrderResponse>> getSummaryPurchaseOrders(
            SearchSummaryPurchaseOrderRequest condition, Pageable pageable) {
        return ResponseEntity.ok(purchaseOrderService.getSummaryPurchaseOrders(condition, pageable));
    }
}
