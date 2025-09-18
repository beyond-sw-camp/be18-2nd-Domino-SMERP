package com.domino.smerp.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class ExcelOrderResponse {
    private String documentNo;   // 전표번호
    private String companyName;  // 거래처명
    private String itemName;     // 품목명
    private BigDecimal qty; // 수량
    private BigDecimal specialPrice; // 단가
    private BigDecimal supplyAmount; // 공급가액
    private String remark;       // 비고
}