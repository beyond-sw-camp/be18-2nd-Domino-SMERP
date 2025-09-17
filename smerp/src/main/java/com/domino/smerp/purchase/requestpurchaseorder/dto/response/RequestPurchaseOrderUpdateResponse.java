package com.domino.smerp.purchase.requestpurchaseorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RequestPurchaseOrderUpdateResponse {

    private final Long rpoId;
    private final LocalDate deliveryDate;
    private final String remark;
    private final String status;
    private final String documentNo;
    private final LocalDateTime updatedAt;
}
