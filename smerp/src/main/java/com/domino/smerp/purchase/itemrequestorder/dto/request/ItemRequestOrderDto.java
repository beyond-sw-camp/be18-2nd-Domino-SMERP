package com.domino.smerp.purchase.itemrequestorder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class ItemRequestOrderDto {
    private final Long itemId;         // 품목 ID
    private final String name;
    private final BigDecimal qty;      // 수량
    private final BigDecimal inboundUnitPrice; // 입고단가
    private final BigDecimal specialPrice; // 요청 시 선택 입력

}
