package com.domino.smerp.order;

import com.domino.smerp.order.dto.request.OrderRequest;
import com.domino.smerp.order.dto.request.UpdateOrderRequest;
import com.domino.smerp.order.dto.response.OrderCreateResponse;
import com.domino.smerp.order.dto.response.OrderDeleteResponse;
import com.domino.smerp.order.dto.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    // ✅ 주문 전체 수정 (PUT)
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long orderId,
            @RequestBody @Valid UpdateOrderRequest request
    ) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, request));
    }

    // ✅ 삭제 (soft-delete 고려 시 patch로 유지 가능)
    @DeleteMapping("/{orderId}") // 또는 @PatchMapping("/{orderId}/delete")
    public ResponseEntity<OrderDeleteResponse> deleteOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.deleteOrder(orderId));
    }
}
