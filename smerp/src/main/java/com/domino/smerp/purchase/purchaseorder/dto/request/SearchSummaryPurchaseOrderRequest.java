package com.domino.smerp.purchase.purchaseorder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class SearchSummaryPurchaseOrderRequest {
    private final String documentNo;   // 전표번호
    private final String companyName;  // 거래처명
    private final String itemName;     // 품목명
    private final BigDecimal qty;      // 수량
    private final BigDecimal inboundUnitPrice; // 입고단가
    private final BigDecimal supplyAmount;    // 공급가액
    private final String remark;       // 비고
    private final LocalDate startDocDate;  // 전표 시작일
    private final LocalDate endDocDate;    // 전표 종료일
}