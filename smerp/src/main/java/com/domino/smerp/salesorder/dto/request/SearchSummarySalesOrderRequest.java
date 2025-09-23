package com.domino.smerp.salesorder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class SearchSummarySalesOrderRequest {
    private final String documentNo;
    private final String itemName;
    private final BigDecimal qty;
    private final BigDecimal specialPrice;
    private final BigDecimal supplyAmount;
    private final BigDecimal tax;
    private final BigDecimal totalAmount;
    private final String clientName;
    private final LocalDate startDocDate;  // 전표 시작일
    private final LocalDate endDocDate;    // 전표 종료일
}
