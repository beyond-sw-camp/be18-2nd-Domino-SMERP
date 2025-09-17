package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderCreateRequest;
import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderUpdateRequest;
import com.domino.smerp.purchase.requestorder.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/request-orders")
public class RequestOrderController {

    private final RequestOrderService requestOrderService;

    // ✅ 발주 생성
    @PostMapping
    public ResponseEntity<RequestOrderCreateResponse> createRequestOrder(
            @RequestBody RequestOrderCreateRequest request
    ) {
        RequestOrderCreateResponse response = requestOrderService.createRequestOrder(request);
        return ResponseEntity.ok(response);
    }

    // ✅ 발주 목록 조회 (페이징)
    @GetMapping
    public ResponseEntity<List<RequestOrderGetListResponse>> getRequestOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<RequestOrderGetListResponse> response = requestOrderService.getRequestOrders(page, size);
        return ResponseEntity.ok(response);
    }

    // ✅ 발주 상세 조회
    @GetMapping("/{requestOrderId}")
    public ResponseEntity<RequestOrderGetDetailResponse> getRequestOrderById(
            @PathVariable Long requestOrderId
    ) {
        RequestOrderGetDetailResponse response = requestOrderService.getRequestOrderById(requestOrderId);
        return ResponseEntity.ok(response);
    }

    // ✅ 발주 수정
    @PatchMapping("/{requestOrderId}")
    public ResponseEntity<RequestOrderUpdateResponse> updateRequestOrder(
            @PathVariable Long requestOrderId,
            @RequestBody RequestOrderUpdateRequest request
    ) {
        RequestOrderUpdateResponse response = requestOrderService.updateRequestOrder(requestOrderId, request);
        return ResponseEntity.ok(response);
    }

    // ✅ 발주 삭제 (소프트 삭제)
    @DeleteMapping("/{requestOrderId}")
    public ResponseEntity<RequestOrderDeleteResponse> deleteRequestOrder(
            @PathVariable Long requestOrderId
    ) {
        RequestOrderDeleteResponse response = requestOrderService.deleteRequestOrder(requestOrderId);
        return ResponseEntity.ok(response);
    }
}
