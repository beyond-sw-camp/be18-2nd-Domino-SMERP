package com.domino.smerp.itemorder.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOrderRequest {
    @NotNull(message = "품목 리스트(items)는 필수 입력입니다.")
    private Long itemId; // 품목 ID
    @NotNull(message = "수량은 필수 입력입니다.")
    @DecimalMin(value = "0.01", message = "수량은 0보다 커야 합니다.")  // 음수차단
    private BigDecimal qty;     // 수량
    private BigDecimal specialPrice;  // 특별가격
}
