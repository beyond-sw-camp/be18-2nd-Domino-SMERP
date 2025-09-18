package com.domino.smerp.order.dto.request;

import com.domino.smerp.order.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class SearchExcelOrderRequest {
    private final String documentNo;
    private final String companyName;
    private final String itemName;
    private final BigDecimal qty;
    private final BigDecimal specialPrice;
    private final BigDecimal supplyAmount;
    private final String remark;
    private final LocalDate startDocDate;  // 전표 시작일
    private final LocalDate endDocDate;    // 전표 종료일
}
