package com.domino.smerp.purchase.purchaseorder.dto.response;

import com.domino.smerp.purchase.purchaseorder.PurchaseOrder;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구매(PurchaseOrder) Response DTO
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PurchaseOrderResponse {

  private Long poId;       // 구매 PK
  private Long roId;       // 발주 전표 ID
  private Instant createdDate;
  private Instant updatedDate;
  private int qty;
  private BigDecimal surtax;
  private BigDecimal price;
  private String remark;

  public static PurchaseOrderResponse from(final PurchaseOrder entity) {
    return PurchaseOrderResponse.builder()
        .poId(entity.getPoId())
        .roId(entity.getRequestOrder().getRoId())
        .createdDate(entity.getCreatedDate())
        .updatedDate(entity.getUpdatedDate())
        .qty(entity.getQty())
        .surtax(entity.getSurtax())
        .price(entity.getPrice())
        .remark(entity.getRemark())
        .build();
  }
}
