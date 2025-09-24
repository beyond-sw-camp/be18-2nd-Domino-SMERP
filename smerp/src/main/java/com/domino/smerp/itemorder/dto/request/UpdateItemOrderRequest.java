package com.domino.smerp.itemorder.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateItemOrderRequest {
    private Long itemOrderId;        // 기존 품목일 경우 PK, 새로 추가된 경우 null
    @NotNull(message = "품목 리스트(items)는 필수 입력입니다.")
    private Long itemId; // 품목 ID
    @NotNull(message = "수량은 필수 입력입니다.")
    private BigDecimal qty;     // 수량
    @NotNull(message = "가격은 필수 입력입니다.")
    private BigDecimal specialPrice;  // 특별가격
}
