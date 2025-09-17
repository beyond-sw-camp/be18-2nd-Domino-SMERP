package com.domino.smerp.purchase.requestorder.dto.response;

import com.domino.smerp.purchase.itemrequestorder.dto.request.ItemRequestOrderDto;
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
public class RequestOrderGetDetailResponse {
    private final Long roId;
    private final Long userId;
    private final Long clientId;
    private final LocalDate deliveryDate;
    private final String status;
    private final String remark;
    private final String documentNo;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final List<ItemRequestOrderDto> items;

    @Getter
    @AllArgsConstructor
    public static class ItemDetail {
        private final Long itemId;
        private final BigDecimal qty;
        private final BigDecimal inboundUnitPrice;
    }
}
