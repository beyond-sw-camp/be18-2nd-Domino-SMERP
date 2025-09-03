package com.domino.smerp.order.dto.response;

import com.domino.smerp.order.Order;
import com.domino.smerp.order.constants.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class OrderResponse {
    private Long orderId;
    private String clientName;
    private String userName;
    private String status;           // ✅ String으로 유지
    private LocalDate deliveryDate;
    private String remark;

    public static OrderResponse from(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .clientName(order.getClient().getCompanyName())
                .userName(order.getUser().getName())
                .status(order.getStatus().name())   // ✅ Enum → String 변환
                .deliveryDate(order.getDeliveryDate())
                .remark(order.getRemark())
                .build();
    }
}
