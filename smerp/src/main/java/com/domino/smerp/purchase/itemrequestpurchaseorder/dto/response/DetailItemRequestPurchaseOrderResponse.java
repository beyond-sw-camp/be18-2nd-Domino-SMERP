package com.domino.smerp.purchase.itemrequestpurchaseorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DetailItemRequestPurchaseOrderResponse {
    private final Long itemId;
    private final String itemName;
    private final String specification;
    private final String unit;
    private final BigDecimal qty;
    private final BigDecimal inboundUnitPrice; // 입고 단가
    private final BigDecimal tax;
    private final BigDecimal totalAmount;
}
