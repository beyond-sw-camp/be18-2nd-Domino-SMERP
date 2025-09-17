package com.domino.smerp.purchase.itemrequestorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ItemRequestOrderCreateRequest {
    private final Long itemId;     // 품목 FK
    private final BigDecimal qty;  // 수량
}
