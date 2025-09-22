package com.domino.smerp.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateReturnOrderResponse {
    private final String documentNo;
    private final String message;

    public static CreateReturnOrderResponse from(String documentNo) {
        return CreateReturnOrderResponse.builder()
                .documentNo(documentNo)
                .message("반품 등록이 완료됐습니다.")
                .build();
    }
}
