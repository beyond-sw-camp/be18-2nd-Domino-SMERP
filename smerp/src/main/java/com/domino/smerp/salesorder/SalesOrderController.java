package com.domino.smerp.salesorder;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.salesorder.dto.request.CreateSalesOrderRequest;
import com.domino.smerp.salesorder.dto.request.SearchSummarySalesOrderRequest;
import com.domino.smerp.salesorder.dto.request.SearchSalesOrderRequest;
import com.domino.smerp.salesorder.dto.request.UpdateSalesOrderRequest;
import com.domino.smerp.salesorder.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    // 판매 등록
    @PostMapping
    public ResponseEntity<CreateSalesOrderResponse> createSalesOrder(
            @RequestBody CreateSalesOrderRequest request) {
        CreateSalesOrderResponse response = salesOrderService.createSalesOrder(request);
        return ResponseEntity.ok(response);
    }

    // 판매 목록 조회
    @GetMapping
    public ResponseEntity<PageResponse<ListSalesOrderResponse>> getSalesOrders(
            SearchSalesOrderRequest condition,
            Pageable pageable) {
        PageResponse<ListSalesOrderResponse> response = salesOrderService.getSalesOrders(condition, pageable);
        return ResponseEntity.ok(response);
    }

    // 판매 상세 조회
    @GetMapping("/{salesOrderId}")
    public ResponseEntity<DetailSalesOrderResponse> getDetailSalesOrder(
            @PathVariable Long salesOrderId) {
        DetailSalesOrderResponse response = salesOrderService.getDetailSalesOrder(salesOrderId);
        return ResponseEntity.ok(response);
    }

    // 판매 수정
    @PatchMapping("/{salesOrderId}")
    public ResponseEntity<UpdateSalesOrderResponse> updateSalesOrder(
            @PathVariable Long salesOrderId,
            @RequestBody UpdateSalesOrderRequest request) {
        UpdateSalesOrderResponse response = salesOrderService.updateSalesOrder(salesOrderId, request);
        return ResponseEntity.ok(response);
    }

    // 판매 삭제
    @DeleteMapping("/{salesOrderId}")
    public ResponseEntity<DeleteSalesOrderResponse> deleteSalesOrder(
            @PathVariable Long salesOrderId) {
        DeleteSalesOrderResponse response = salesOrderService.deleteSalesOrder(salesOrderId);
        return ResponseEntity.ok(response);
    }

    // 판매 현황 조회
    @GetMapping("/summary")
    public ResponseEntity<List<SummarySalesOrderResponse>> getSalesOrderSummary(
            SearchSummarySalesOrderRequest condition,
            Pageable pageable
    ) {
        return ResponseEntity.ok(salesOrderService.getSummarySalesOrder(condition, pageable));
    }
}