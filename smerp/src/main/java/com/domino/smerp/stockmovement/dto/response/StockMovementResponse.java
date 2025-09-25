package com.domino.smerp.stockmovement.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StockMovementResponse {

  //일자
  private final Instant createdAt;

  //거래처명
  private final String companyName;

  //품목명
  private final String itemName;

  //입고수량
  private final BigDecimal inboundQty;

  //출고수량
  private final BigDecimal outboundQty;

  //출고수량
  private final BigDecimal totalQty;
}
