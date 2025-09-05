package com.domino.smerp.order.dto.response;

import com.domino.smerp.order.Order;
import com.domino.smerp.order.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderCreateResponse {
    private final Long orderId;
    private final OrderStatus status;
    private final String message;

    public static OrderCreateResponse from(Order order) {
        return OrderCreateResponse.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .message("주문 등록이 완료됐습니다.")
                .build();
    }
}
