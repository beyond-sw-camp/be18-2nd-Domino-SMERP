package com.domino.smerp.salesorder.dto.response;

import com.domino.smerp.salesorder.SalesOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class SalesOrderResponse {
    private final Long soId;          // 판매 ID
    private final Long orderId;       // 주문 ID
    private final BigDecimal totalAmount; // 총 공급가
    private final String remark;      // 적요
    private final String status;      // 주문 상태
    private final Instant createdDate; // 생성일자

    public static SalesOrderResponse from(SalesOrder salesOrder) {
        // 주문 참조
        var order = salesOrder.getOrder();

        return SalesOrderResponse.builder()
                .soId(salesOrder.getSoId())
                .orderId(order.getOrderId())
                .totalAmount(order.getTotalAmount()) // ✅ 주문에서 계산된 총 공급가
                .remark(order.getRemark())
                .status(order.getStatus().name())    // ✅ 주문 상태 그대로 노출
                .createdDate(order.getCreatedAt())
                .build();
    }
}
