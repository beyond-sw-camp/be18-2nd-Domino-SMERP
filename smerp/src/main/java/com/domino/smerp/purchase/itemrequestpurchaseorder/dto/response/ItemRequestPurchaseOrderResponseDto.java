package com.domino.smerp.purchase.itemrequestpurchaseorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ItemRequestPurchaseOrderResponseDto {
    private final Long itemRequestPurchaseOrderId; // 교차테이블 PK
    private final Long itemId;                     // 품목 FK
    private final String itemName;                 // 품목명 (조회 시 조인)
    private final String specification;            // 규격
    private final String unit;                     // 단위
    private final BigDecimal qty;                  // 수량
    private final BigDecimal inboundUnitPrice;     // 입고 단가
}
