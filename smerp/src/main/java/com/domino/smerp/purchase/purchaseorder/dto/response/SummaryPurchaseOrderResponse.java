package com.domino.smerp.purchase.purchaseorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class SummaryPurchaseOrderResponse {

    private final String documentNo;   // 전표번호
    private final String companyName;  // 거래처명
    private final String itemName;     // 품목명
    private final BigDecimal qty;      // 수량
    private final BigDecimal inboundUnitPrice; // 입고단가
    private final BigDecimal supplyAmount;     // 공급가액 (qty * inboundUnitPrice)
    private final String remark;       // 비고
}
