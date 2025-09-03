package com.domino.smerp.purchase.requestpurchaseorder.dto;

import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrderStatus;

import java.time.LocalDate;

public record RequestPurchaseOrderResponse(
    Long id,
    Long userId,
    LocalDate createdDate,
    LocalDate updatedDate,
    LocalDate deliveryDate,
    String remark,
    RequestPurchaseOrderStatus status
) {

  public static RequestPurchaseOrderResponse from(RequestPurchaseOrder e) {
    return new RequestPurchaseOrderResponse(
        e.getId(),
        e.getUserId(),
        e.getCreatedDate(),
        e.getUpdatedDate(),
        e.getDeliveryDate(),
        e.getRemark(),
        e.getStatus()
    );
  }
}
