package com.domino.smerp.order.dto.response;

import com.domino.smerp.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DeleteOrderResponse {
    private final String documentNo;
    private final String message;

    public static DeleteOrderResponse from(Order order) {
        return DeleteOrderResponse.builder()
                .documentNo(order.getDocumentNo())
                .message("주문이 삭제가 완료됐습니다.")
                .build();
    }
}
