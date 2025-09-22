package com.domino.smerp.purchase.requestpurchaseorder.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RequestPurchaseOrderGetListResponse {
    private final String documentNo;

    private final String empNo;     // 사번

    private final String itemName;

    private final BigDecimal totalQty;

    private final LocalDate deliveryDate;

    private final String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant createdAt;
}
