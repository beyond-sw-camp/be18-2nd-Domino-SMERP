package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.purchase.requestorder.dto.request.SearchRequestOrderRequest;
import com.domino.smerp.purchase.requestorder.dto.response.RequestOrderGetListResponse;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.RequestPurchaseOrderCreateRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.RequestPurchaseOrderUpdateRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.SearchRequestPurchaseOrderRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/request-purchase-orders")
public class RequestPurchaseOrderController {

    private final RequestPurchaseOrderService requestPurchaseOrderService;

    // ✅ 구매요청 생성
    @PostMapping
    public ResponseEntity<RequestPurchaseOrderCreateResponse> createRequestPurchaseOrder(
            @RequestBody RequestPurchaseOrderCreateRequest request
    ) {
        RequestPurchaseOrderCreateResponse response = requestPurchaseOrderService.createRequestPurchaseOrder(request);
        return ResponseEntity.ok(response);
    }

    // ✅ 구매요청 목록 조회 (페이징)
    @GetMapping
    public ResponseEntity<PageResponse<RequestPurchaseOrderGetListResponse>> getRequestPurchaseOrders(
            @ModelAttribute SearchRequestPurchaseOrderRequest keyword,
            Pageable pageable
    ) {
        return ResponseEntity.ok(requestPurchaseOrderService.searchRequestPurchaseOrders(keyword, pageable));
    }

    // ✅ 구매요청 상세 조회
    @GetMapping("/{rpoId}")
    public ResponseEntity<RequestPurchaseOrderGetDetailResponse> getRequestPurchaseOrderById(
            @PathVariable Long rpoId
    ) {
        RequestPurchaseOrderGetDetailResponse response = requestPurchaseOrderService.getRequestPurchaseOrderById(rpoId);
        return ResponseEntity.ok(response);
    }

    // ✅ 구매요청 수정
    @PatchMapping("/{rpoId}")
    public ResponseEntity<RequestPurchaseOrderUpdateResponse> updateRequestPurchaseOrder(
            @PathVariable Long rpoId,
            @RequestBody RequestPurchaseOrderUpdateRequest request
    ) {
        RequestPurchaseOrderUpdateResponse response = requestPurchaseOrderService.updateRequestPurchaseOrder(rpoId, request);
        return ResponseEntity.ok(response);
    }

    // ✅ 구매요청 삭제 (소프트 삭제)
    @DeleteMapping("/{rpoId}")
    public ResponseEntity<RequestPurchaseOrderDeleteResponse> deleteRequestPurchaseOrder(
            @PathVariable Long rpoId
    ) {
        RequestPurchaseOrderDeleteResponse response = requestPurchaseOrderService.deleteRequestPurchaseOrder(rpoId);
        return ResponseEntity.ok(response);
    }
}
