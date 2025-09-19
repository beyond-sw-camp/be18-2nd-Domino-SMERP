package com.domino.smerp.order;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.order.dto.request.*;
import com.domino.smerp.order.dto.response.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // 주문 등록 (POST)
    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    // 주문 목록 조회 (GET)
    @GetMapping
    public ResponseEntity<PageResponse<ListOrderResponse>> getOrders(SearchOrderRequest condition, Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrders(condition, pageable));
    }

    // 주문 상세 조회 (GET)
    @GetMapping("/{orderId}")
    public ResponseEntity<DetailOrderResponse> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getDetailOrder(orderId));
    }

    // 주문 수정 (PATCH)
    @PatchMapping("/{orderId}")
    public ResponseEntity<UpdateOrderResponse> updateOrder(@PathVariable Long orderId, @RequestBody @Valid UpdateOrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, request));
    }

    // 삭제 (soft-delete 고려 시 patch로 유지 가능)
    @DeleteMapping("/{orderId}")
    public ResponseEntity<DeleteOrderResponse> deleteOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.deleteOrder(orderId));
    }

    // 주문 현황 (GET)
    @GetMapping("/summary")
    public ResponseEntity<List<SummaryOrderResponse>> getOrderSummary(SearchSummaryOrderRequest condition, Pageable pageable) {
        return ResponseEntity.ok(orderService.getSummaryOrder(condition, pageable));
    }

    // 반품 등록 (POST)
    @PostMapping("/returns")
    public ResponseEntity<CreateReturnOrderResponse> createReturn(@Valid @RequestBody CreateReturnOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createReturnOrder(request));
    }

    // 반품 현황 조회 (GET)
    @GetMapping("/returns/summary")
    public ResponseEntity<List<SummaryReturnOrderResponse>> getReturnOrders(SearchSummaryReturnOrderRequest condition, Pageable pageable) {
        return ResponseEntity.ok(orderService.getSummaryReturnOrders(condition, pageable));
    }
}
