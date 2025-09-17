package com.domino.smerp.purchase.itemrequestorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class DetailItemRequestOrderResponse {

    private final Long itemId;          // 품목 PK
    private final String itemCode;      // 품목 코드
    private final String itemName;      // 품목명
    private final String specification; // 규격
    private final String unit;          // 단위
    private final BigDecimal qty;       // 발주 수량
    private final BigDecimal unitPrice; // 단가 (입고단가 or 출고단가)
    private final BigDecimal supplyAmount; // 공급가액 (qty * unitPrice)
    private final BigDecimal tax;       // 부가세
    private final BigDecimal totalAmount; // 총액 (공급가액 + 부가세)
    private final LocalDate deliveryDate; // 납기일자
    private final String remark;          // 비고
}
