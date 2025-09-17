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
public class RequestOrderGetListResponse {
    private final Long roId;
    private final Long clientId;
    private final String itemName;   // 품목명 or "XXX 외 n건"
    private final BigDecimal totalQty; // ✅ 품목 수량 총합
    private final LocalDate deliveryDate;
    private final String status;
    private final String documentNo;
    private final Instant createdAt;
}
