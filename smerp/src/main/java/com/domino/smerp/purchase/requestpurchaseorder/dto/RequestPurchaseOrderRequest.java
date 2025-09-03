package com.domino.smerp.purchase.requestpurchaseorder.dto;

import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RequestPurchaseOrderRequest(
    @NotNull Long userId,
    LocalDate deliveryDate,
    @Size(max = 100) String remark,
    RequestPurchaseOrderStatus status,
    LocalDate updatedDate    // 전표일자(선택). null이면 @PrePersist가 created_date로 세팅
) {

}
