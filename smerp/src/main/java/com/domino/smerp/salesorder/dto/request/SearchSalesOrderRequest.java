package com.domino.smerp.salesorder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class SearchSalesOrderRequest {
    private final String companyName;
    private final String userName;
    private final String documentNo;
    private final String warehouseName;
    private final String remark;
    private final LocalDate startDocDate;  // 전표 시작일
    private final LocalDate endDocDate;    // 전표 종료일
}
