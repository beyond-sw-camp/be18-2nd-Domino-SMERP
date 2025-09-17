package com.domino.smerp.purchase.requestpurchaseorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RequestPurchaseOrderGetDetailResponse {

    private final Long rpoId;
    private final Long userId;
    private final LocalDate deliveryDate;
    private final String status;
    private final String remark;
    private final String documentNo;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private final List<ItemDetail> items; // 구매요청 품목 상세 리스트

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ItemDetail {
        private final Long itemId;
        private final Double qty;
        private final Double inboundUnitPrice;
    }
}
