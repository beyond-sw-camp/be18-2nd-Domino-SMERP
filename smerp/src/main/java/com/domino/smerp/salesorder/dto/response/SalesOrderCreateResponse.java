package com.domino.smerp.salesorder.dto.response;

import com.domino.smerp.order.Order;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.order.dto.response.OrderCreateResponse;
import com.domino.smerp.salesorder.SalesOrder;
import com.domino.smerp.salesorder.constants.SalesOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SalesOrderCreateResponse {
    private final Long soId;
    private final SalesOrderStatus status;
    private final String message;

    public static SalesOrderCreateResponse from(SalesOrder salesOrder) {
        return SalesOrderCreateResponse.builder()
                .soId(salesOrder.getSoId())
                .status(salesOrder.getStatus())
                .message("판매 등록이 완료됐습니다.")
                .build();
    }
}
