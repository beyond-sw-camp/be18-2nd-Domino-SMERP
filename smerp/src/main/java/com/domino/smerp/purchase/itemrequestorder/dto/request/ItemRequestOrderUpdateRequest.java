package com.domino.smerp.purchase.itemrequestorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ItemRequestOrderUpdateRequest {
    private final BigDecimal qty;  // 변경할 수량
}
