package com.domino.smerp.itemorder.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOrderRequest {
    private Long itemId; // 품목 ID
    private BigDecimal qty;     // 수량
}
