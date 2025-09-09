package com.domino.smerp.purchase.purchaseorder.dto.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PurchaseOrderCreateRequest {

  private final Long roId;
  private final BigDecimal qty;
  private final BigDecimal surtax;
  private final BigDecimal price;
  private final String remark;
  private final String documentNo;
}
