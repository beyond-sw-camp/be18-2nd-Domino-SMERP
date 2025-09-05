package com.domino.smerp.itemorder.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOrderRequest {
    private Long itemId; // 품목 ID
    private int qty;     // 수량
}
