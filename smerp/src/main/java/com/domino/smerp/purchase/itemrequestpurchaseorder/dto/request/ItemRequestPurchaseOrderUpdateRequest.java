package com.domino.smerp.purchase.itemrequestpurchaseorder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ItemRequestPurchaseOrderUpdateRequest {
    private final Long itemId;         // 품목 ID
    private final String name;
    private final BigDecimal qty;  // 변경할 수량
    private final BigDecimal specialPrice; // 요청 시 선택 입력

}
