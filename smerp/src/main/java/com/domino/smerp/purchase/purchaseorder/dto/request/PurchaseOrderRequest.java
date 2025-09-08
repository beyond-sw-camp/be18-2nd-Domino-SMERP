package com.domino.smerp.purchase.purchaseorder.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구매(PurchaseOrder) 등록/수정 Request DTO
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PurchaseOrderRequest {

  @NotNull
  private Long roId; // 발주 전표 ID (RequestOrder FK)

  // qty는 양수(구매), 음수(반품) 모두 가능 → 제약 조건 제거
  private int qty; // 수량

  @NotNull
  private BigDecimal surtax; // 부가세

  @NotNull
  private BigDecimal price; // 금액 (qty * 단가)

  @Size(max = 100)
  private String remark; // 비고
}
