// file: dto/response/PurchaseOrderDetailItemResponse.java
package com.domino.smerp.purchase.purchaseorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PurchaseOrderDetailItemResponse {
    private final String itemName;
    private final BigDecimal qty;
    private final BigDecimal inboundUnitPrice;
    private final BigDecimal supplyAmount; // qty * inboundUnitPrice
}
