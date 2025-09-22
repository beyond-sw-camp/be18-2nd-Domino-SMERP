package com.domino.smerp.purchase.requestpurchaseorder.dto.response;

import com.domino.smerp.purchase.itemrequestpurchaseorder.dto.request.ItemRequestPurchaseOrderDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RequestPurchaseOrderGetDetailResponse {
    private final String documentNo;

    private final String empNo;     // 사번

    private final LocalDate deliveryDate;

    private final String status;

    private final String remark;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant updatedAt;

    private final List<ItemRequestPurchaseOrderDto> items;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ItemDetail {
        private final Long itemId;
        private final Double qty;
        private final Double inboundUnitPrice;
        private final BigDecimal specialPrice; // 요청 시 선택 입력

    }
}
