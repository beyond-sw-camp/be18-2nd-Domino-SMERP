package com.domino.smerp.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class ExcelReturnOrderResponse {
    private final String documentNo;   // 반품번호
    private final String companyName;  // 거래처
    private final String itemName;     // 품목명
    private final BigDecimal qty;      // 수량
    private final BigDecimal specialPrice; // 단가
    private final BigDecimal refundAmount; // 환불금액 (qty * specialPrice)
    private final String remark;       // 반품 사유
}
