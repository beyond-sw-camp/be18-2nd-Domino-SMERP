package com.domino.smerp.purchase.requestorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RequestOrderGetListResponse {
    private final Long roId;
    private final Long clientId;
    private final Long itemId;
    private final BigDecimal qty;
    private final String status;
    private final String documentNo;
    private final Instant createdAt;
}
