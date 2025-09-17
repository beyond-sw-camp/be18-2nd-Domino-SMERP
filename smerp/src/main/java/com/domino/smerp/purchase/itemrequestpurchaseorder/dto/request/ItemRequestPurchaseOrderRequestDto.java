package com.domino.smerp.purchase.itemrequestpurchaseorder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ItemRequestPurchaseOrderRequestDto {
    private final Long itemId;              // 품목 FK
    private final BigDecimal qty;           // 수량
    private final BigDecimal inboundUnitPrice; // 입고 단가
}
