package com.domino.smerp.purchase.purchaseorder.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PurchaseOrderGetDetailResponse {
    private final String documentNo;

    private String empNo; // 사번

    private final String companyName;  // 거래처 회사명

    private final String warehouseName;

    private final BigDecimal qty;

    private final BigDecimal inboundUnitPrice;

    private final BigDecimal specialPrice; // 요청 시 선택 입력

    private final BigDecimal surtax;

    private final BigDecimal price;

    private final String remark;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant updatedAt;
}
