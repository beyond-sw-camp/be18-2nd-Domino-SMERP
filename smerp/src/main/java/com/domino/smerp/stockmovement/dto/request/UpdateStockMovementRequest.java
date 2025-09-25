package com.domino.smerp.stockmovement.dto.request;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UpdateStockMovementRequest {
  private final String itemName;
  private final BigDecimal movedQty;
  private final String userName;
}
