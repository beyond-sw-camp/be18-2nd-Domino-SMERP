package com.domino.smerp.itemorder.dto.response;

import com.domino.smerp.itemorder.ItemOrder;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ItemOrderResponse {
    private Long itemId;
    private String itemName;
    private BigDecimal qty;

    public static ItemOrderResponse from(ItemOrder entity) {
        return ItemOrderResponse.builder()
                .itemId(entity.getItem().getItemId())
                .itemName(entity.getItem().getName())
                .qty(entity.getQty())
                .build();
    }
}
