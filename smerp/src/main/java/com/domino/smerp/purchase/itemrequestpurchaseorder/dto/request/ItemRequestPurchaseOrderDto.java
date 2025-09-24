package com.domino.smerp.purchase.itemrequestpurchaseorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ItemRequestPurchaseOrderDto {
    @NotNull(message = "품목 ID는 필수입니다.")
    private final Long itemId;

    @NotBlank(message = "품목명은 필수입니다.")
    private final String name;

    @NotNull(message = "수량은 필수입니다.")
    private final BigDecimal qty;

    @NotNull(message = "입고 단가는 필수입니다.")
    private final BigDecimal inboundUnitPrice;

    private final BigDecimal specialPrice; // 요청 시 선택 입력

}
