package com.domino.smerp.itemorder.dto;

import com.domino.smerp.itemorder.ItemOrderCrossedTable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemOrderResponse {
    private Long itemOrderId;   // 교차 테이블 PK
    private Long orderId;       // 주문 ID
    private Long itemId;        // 품목 ID
    private String itemName;    // 품목명
    private Integer qty;        // 수량

    public static ItemOrderResponse from(ItemOrderCrossedTable entity) {
        return ItemOrderResponse.builder()
                .itemOrderId(entity.getItemOrderId())
                .orderId(entity.getOrder().getOrderId())
                .itemId(entity.getItem().getItemId())
                .itemName(entity.getItem().getName()) // Item 엔티티에 name 필드 있다고 가정
                .qty(entity.getQty())
                .build();
    }
}
