package com.domino.smerp.purchase.requestpurchaseorder.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구매요청(RequestPurchaseOrder) 등록/수정 Request DTO
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestPurchaseOrderRequest {

  @NotNull
  private Long userId;

  @NotNull
  private Instant deliveryDate;

  @Size(max = 100)
  private String remark;

  private String status;

  private String documentNo;

  // ===== 라인 요청 DTO =====
  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class RequestPurchaseOrderLineRequest {

    @NotNull
    private Long itemId;

    @NotNull
    private int qty;
  }
}
