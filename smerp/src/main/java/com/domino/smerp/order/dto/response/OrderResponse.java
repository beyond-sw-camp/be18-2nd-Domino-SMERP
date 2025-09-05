package com.domino.smerp.order.dto.response;

import com.domino.smerp.order.Order;
import com.domino.smerp.itemorder.dto.response.ItemOrderResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)  // ✅ Jackson 직렬화/역직렬화 지원
public class OrderResponse {

    private final Long orderId;
    private final String clientName;
    private final String userName;
    private final String status;
    private final LocalDate deliveryDate;
    private final String remark;
    private final List<ItemOrderResponse> items; // ✅ 불변 리스트

    public static OrderResponse from(Order order) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .clientName(order.getClient().getCompanyName())
                .userName(order.getUser().getName())
                .status(order.getStatus().name())
                .deliveryDate(order.getDeliveryDate())
                .remark(order.getRemark())
                .items(order.getOrderItems().stream()
                        .map(ItemOrderResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
