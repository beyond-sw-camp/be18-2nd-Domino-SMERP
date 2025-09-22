package com.domino.smerp.purchase.requestorder.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
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
    private final String documentNo;

    private String empNo; // 사번

    private final String companyName;  // 거래처 회사명

    private final String itemName;   // 품목명 or "XXX 외 n건"

    private final BigDecimal totalQty; // 품목 수량 총합

    private final LocalDate deliveryDate;

    private final String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant createdAt;
}
