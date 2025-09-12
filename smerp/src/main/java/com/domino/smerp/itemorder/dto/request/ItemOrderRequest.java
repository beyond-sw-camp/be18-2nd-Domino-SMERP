package com.domino.smerp.itemorder.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOrderRequest {
    @NotNull(message = "품목 리스트(items)는 필수 입력입니다.")
    private Long itemId; // 품목 ID
    @NotNull(message = "수량 필수 입력입니다.")
    private BigDecimal qty;     // 수량
}
