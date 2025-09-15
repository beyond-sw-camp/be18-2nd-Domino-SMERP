package com.domino.smerp.purchase.requestorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RequestOrderGetDetailResponse {
    private final Long roId;
    private final Long userId;
    private final Long clientId;
    private final Long itemId;
    private final BigDecimal qty;
    private final LocalDate deliveryDate;
    private final String status;
    private final String remark;
    private final String documentNo;
    private final Instant createdAt;
    private final Instant updatedAt;
}
