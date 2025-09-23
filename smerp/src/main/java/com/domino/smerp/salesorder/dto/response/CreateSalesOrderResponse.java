package com.domino.smerp.salesorder.dto.response;

import com.domino.smerp.salesorder.SalesOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateSalesOrderResponse {
    private final String documentNo;
    private final String message;

    public static CreateSalesOrderResponse from(SalesOrder salesOrder) {
        return CreateSalesOrderResponse.builder()
                .documentNo(salesOrder.getDocumentNo())
                .message("판매 등록이 완료됐습니다.")
                .build();
    }
}
