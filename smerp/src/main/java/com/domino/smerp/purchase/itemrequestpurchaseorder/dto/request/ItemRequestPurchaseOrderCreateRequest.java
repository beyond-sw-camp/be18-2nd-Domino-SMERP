package com.domino.smerp.purchase.itemrequestpurchaseorder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ItemRequestPurchaseOrderCreateRequest {
    private final Long itemId;
    private final BigDecimal qty;
    private final BigDecimal inboundUnitPrice; // 입고 단가
}
