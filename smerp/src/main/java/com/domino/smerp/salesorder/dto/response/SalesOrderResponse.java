package com.domino.smerp.salesorder.dto.response;

import com.domino.smerp.salesorder.SalesOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class SalesOrderResponse {
    private Long soId;
    private Long orderId;
    private Integer qty;
    private Double surtax;
    private Double price;
    private String remark;
    private String status;          // ✅ String 유지
    private LocalDate createdDate;

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
