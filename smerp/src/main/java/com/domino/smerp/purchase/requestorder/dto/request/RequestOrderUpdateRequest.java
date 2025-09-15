package com.domino.smerp.purchase.requestorder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RequestOrderUpdateRequest {
    private final BigDecimal qty;
    private final LocalDate deliveryDate;
    private final String remark;
    private final String status; // 상태 변경 (pending/approved/completed/returned)
    private final LocalDate newDocDate; // 전표 날짜 변경 (null이면 변경 없음)
}
