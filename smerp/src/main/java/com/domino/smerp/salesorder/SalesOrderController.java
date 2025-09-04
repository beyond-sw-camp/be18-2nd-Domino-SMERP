package com.domino.smerp.salesorder;

import com.domino.smerp.salesorder.SalesOrderService;
import com.domino.smerp.salesorder.dto.request.SalesOrderRequest;
import com.domino.smerp.salesorder.dto.response.SalesOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    @PostMapping
    public ResponseEntity<SalesOrderResponse> createSalesOrder(@RequestBody SalesOrderRequest request) {
        return ResponseEntity.ok(salesOrderService.createSalesOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<SalesOrderResponse>> getSalesOrders() {
        return ResponseEntity.ok(salesOrderService.getSalesOrders());
    }

    @GetMapping("/{soId}")
    public ResponseEntity<SalesOrderResponse> getSalesOrder(@PathVariable Long soId) {
        return ResponseEntity.ok(salesOrderService.getSalesOrderById(soId));
    }

    // ✅ 판매 수정
    @PatchMapping("/{soId}")
    public ResponseEntity<SalesOrderResponse> updateSalesOrder(
            @PathVariable Long soId,
            @RequestBody SalesOrderRequest request) {
        return ResponseEntity.ok(salesOrderService.updateSalesOrder(soId, request));
    }

    // ✅ 판매 삭제
    @DeleteMapping("/{soId}")
    public ResponseEntity<Void> deleteSalesOrder(@PathVariable Long soId) {
        salesOrderService.deleteSalesOrder(soId);
        return ResponseEntity.noContent().build();
    }
}
