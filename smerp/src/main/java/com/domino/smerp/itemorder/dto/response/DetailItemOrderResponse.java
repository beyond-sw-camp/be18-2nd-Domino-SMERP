package com.domino.smerp.itemorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class DetailItemOrderResponse {
    private Long itemCode;       // 품목코드1
    private String itemName;       // 품목명
    private String specification;  // 규격
    private BigDecimal qty;        // 수량
    private String unit;           // 단위
    private BigDecimal specialPrice;  // 단가
    private BigDecimal supplyAmount; // 공급가액
    private BigDecimal tax;        // 부가세
    private BigDecimal totalAmount; // 금액
    private LocalDate deliveryDate; // 납기일자
    private String remark;           // 적요

}
