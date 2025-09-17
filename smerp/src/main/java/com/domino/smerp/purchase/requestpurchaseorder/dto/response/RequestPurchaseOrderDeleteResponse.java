package com.domino.smerp.purchase.requestpurchaseorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RequestPurchaseOrderDeleteResponse {

    private final Long rpoId;
    private final boolean isDeleted;
    private final LocalDateTime deletedAt;
    private final String message;
}
