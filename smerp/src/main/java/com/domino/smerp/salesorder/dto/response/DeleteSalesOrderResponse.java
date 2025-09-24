package com.domino.smerp.salesorder.dto.response;

import com.domino.smerp.salesorder.SalesOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DeleteSalesOrderResponse {
    private final String documentNo;
    private final String message;

    public static DeleteSalesOrderResponse from(SalesOrder so) {
        return DeleteSalesOrderResponse.builder()
                .documentNo(so.getDocumentNo())
                .message("판매 전표가 삭제되었습니다.")
                .build();
    }
}
