package com.domino.smerp.purchase.purchaseorder.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PurchaseOrderUpdateResponse {

  private final Long poId;
  private final BigDecimal qty;
  private final String remark;
  private final Instant updatedAt;
  private final String documentNo;
}
