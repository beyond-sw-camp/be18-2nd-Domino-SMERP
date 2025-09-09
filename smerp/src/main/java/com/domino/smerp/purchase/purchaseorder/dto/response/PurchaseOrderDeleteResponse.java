package com.domino.smerp.purchase.purchaseorder.dto.response;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PurchaseOrderDeleteResponse {

  private final Long poId;
  private final boolean isDeleted;
  private final Instant deletedAt;
  private final String message;
}
