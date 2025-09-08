package com.domino.smerp.salesorder.dto.response;

import com.domino.smerp.salesorder.SalesOrder;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class SalesOrderResponse {

    private final Long soId;
    private final Long orderId;
    private final Integer qty;
    private final BigDecimal surtax;
    private final BigDecimal price;
    private final String remark;
    private final String status;          // ✅ String 유지
    private final LocalDate createdDate;

    public static SalesOrderResponse from(SalesOrder salesOrder) {
        return SalesOrderResponse.builder()
                .soId(salesOrder.getSoId())
                .orderId(salesOrder.getOrder().getOrderId())
                .qty(salesOrder.getQty())
                .surtax(salesOrder.getSurtax())
                .price(salesOrder.getPrice())
                .remark(salesOrder.getRemark())
                .status(salesOrder.getStatus().name())   // ✅ Enum → String 변환
                .createdDate(salesOrder.getCreatedDate())
                .build();
    }
}
