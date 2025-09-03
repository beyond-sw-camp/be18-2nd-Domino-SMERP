package com.domino.smerp.itemorder.dto;

import lombok.Getter;

@Getter
public class ItemOrderRequest {
    private Long orderId;   // 주문 ID
    private Long itemId;    // 품목 ID
    private Integer qty;    // 수량
}
