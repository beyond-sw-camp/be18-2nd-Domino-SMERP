package com.domino.smerp.order.dto.response;

import com.domino.smerp.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderDeleteResponse {
    private final Long orderId;
    private final String message;

    public static OrderDeleteResponse from(Order order) {
        return OrderDeleteResponse.builder()
                .orderId(order.getOrderId())
                .message("주문이 삭제가 완료됐습니다.")
                .build();
    }
}
