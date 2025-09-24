package com.domino.smerp.salesorder.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class UpdateSalesOrderRequest {

    @NotNull(message = "주문일자(document)는 필수 입력입니다.")
    private final LocalDate documentDate;  // 판매일자

    @Size(max = 100, message = "비고는 최대 100자까지 입력 가능합니다.")
    private final String remark;

    @NotNull(message = "출하창고는 필수 입력입니다.")
    private final String warehouseName;
}