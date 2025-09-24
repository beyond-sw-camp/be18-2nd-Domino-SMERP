package com.domino.smerp.purchase.requestorder.dto.response;

import com.domino.smerp.purchase.itemrequestorder.dto.request.ItemRequestOrderDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RequestOrderCreateResponse {
    private final String documentNo;

    private String empNo; // 사번

    private final String companyName;  // 거래처 회사명

    private final LocalDate deliveryDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant createdAt;

    private final List<ItemDetail> items;

    @Getter
    @AllArgsConstructor
    public static class ItemDetail {
        private final Long itemId;

        private final String name;

        private final BigDecimal qty;

        private final BigDecimal inboundUnitPrice;

        private final BigDecimal specialPrice; // 요청 시 선택 입력

    }
}


