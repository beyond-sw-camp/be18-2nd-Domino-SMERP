package com.domino.smerp.salesorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class ExcelSalesOrderResponse {
    private final String documentNo;
    private final String itemName;
    private final BigDecimal qty;
    private final BigDecimal specialPrice;
    private final BigDecimal supplyAmount; // 추가
    private final BigDecimal tax;
    private final BigDecimal totalAmount;
    private final String clientName;
}
