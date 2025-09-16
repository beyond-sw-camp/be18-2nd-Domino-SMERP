package com.domino.smerp.order.dto.request;

import com.domino.smerp.order.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class OrderSearchRequest {
    private final String companyName;
    private final OrderStatus status;
    private final String userName;
    private final String documentNo;
    private final String remark;
    private final LocalDate startDocDate;  // 전표 시작일
    private final LocalDate endDocDate;    // 전표 종료일
}