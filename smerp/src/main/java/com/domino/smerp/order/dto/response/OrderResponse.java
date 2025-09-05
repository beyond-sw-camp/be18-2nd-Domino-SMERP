package com.domino.smerp.order.dto.response;

import com.domino.smerp.order.Order;
import com.domino.smerp.itemorder.dto.response.ItemOrderResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class OrderResponse {
    private Long orderId;
    private String clientName;
    private String userName;
    private String status;
    private LocalDate deliveryDate;
    private String remark;
    private List<ItemOrderResponse> items; // ✅ 품목 리스트 추가

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
                        .collect(Collectors.toList())) // ✅ 품목 매핑
                .build();
    }
}
