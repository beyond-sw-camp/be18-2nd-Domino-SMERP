// file: java/com/domino/smerp/salesorder/dto/response/UpdateSalesOrderResponse.java
package com.domino.smerp.salesorder.dto.response;

import com.domino.smerp.salesorder.SalesOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateSalesOrderResponse {
    private final String documentNo;
    private final String message;

    public static UpdateSalesOrderResponse from(SalesOrder so) {
        return UpdateSalesOrderResponse.builder()
                .documentNo(so.getDocumentNo())
                .message("판매 수정이 완료됐습니다.")
                .build();
    }
}
