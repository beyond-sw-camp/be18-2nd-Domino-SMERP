package com.domino.smerp.purchase.requestorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RequestOrderUpdateResponse {
    private final Long roId;
    private final BigDecimal qty;
    private final String remark;
    private final String status;
    private final String documentNo;
    private final Instant updatedAt;
}
