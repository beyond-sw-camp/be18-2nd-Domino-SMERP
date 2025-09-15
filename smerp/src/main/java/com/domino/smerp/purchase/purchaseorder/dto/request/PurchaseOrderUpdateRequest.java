package com.domino.smerp.purchase.purchaseorder.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PurchaseOrderUpdateRequest {

  private final BigDecimal qty;
  private final BigDecimal surtax;
  private final BigDecimal price;
  private final String remark;
  private final LocalDate newDocDate; // yyyy/MM/dd, 전표 날짜 변경 시 사용 (null이면 변경 없음)
}
