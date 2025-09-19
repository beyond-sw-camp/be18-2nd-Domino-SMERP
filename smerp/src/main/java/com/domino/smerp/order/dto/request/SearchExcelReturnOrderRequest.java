package com.domino.smerp.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class SearchExcelReturnOrderRequest {
    private final String documentNo;   // 반품 번호
    private final String companyName;  // 거래처명
    private final String itemName;     // 품목명
    private final BigDecimal qty;      // 수량
    private final BigDecimal specialPrice; // 단가
    private final BigDecimal refundAmount; // 환불금액
    private final String remark;       // 반품 사유
    private final LocalDate startDocDate;  // 시작일
    private final LocalDate endDocDate;    // 종료일
}
