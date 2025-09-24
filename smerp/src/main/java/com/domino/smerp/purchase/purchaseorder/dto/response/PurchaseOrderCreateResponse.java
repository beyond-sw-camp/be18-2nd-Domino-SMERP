package com.domino.smerp.purchase.purchaseorder.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PurchaseOrderCreateResponse {
    private final String documentNo;

    private String empNo; // 사번

    private final String companyName;  // 거래처 회사명

    private final String warehouseName;

    private final BigDecimal qty;

    private final BigDecimal inboundUnitPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
  private final Instant createdAt;
}
