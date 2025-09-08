package com.domino.smerp.order.dto.response;

import com.domino.smerp.order.Order;
import com.domino.smerp.order.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderCreateResponse {
    private final String documentNo;   // ✅ 전표 번호로 변경
    private final OrderStatus status;
    private final String message;

    public static OrderCreateResponse from(Order order) {
        return OrderCreateResponse.builder()
                .documentNo(order.getDocumentNo())   // ✅ 전표번호 세팅
                .status(order.getStatus())
                .message("주문 등록이 완료됐습니다.")
                .build();
    }
}
