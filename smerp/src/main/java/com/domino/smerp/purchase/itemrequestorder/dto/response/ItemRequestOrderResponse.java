package com.domino.smerp.purchase.itemrequestorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ItemRequestOrderResponse {
    private final Long itemRoId;
    private final Long itemId;
    private final String itemName;
    private final BigDecimal qty;
}
