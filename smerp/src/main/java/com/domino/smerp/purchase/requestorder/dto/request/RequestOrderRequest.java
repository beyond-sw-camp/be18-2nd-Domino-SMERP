package com.domino.smerp.purchase.requestorder.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestOrderRequest {

  @NotNull
  private final Long userId;

  @NotNull
  private final Long clientId;

  private final Long rpoId;

  @NotNull
  private final Instant deliveryDate;

  @Size(max = 100)
  private final String remark;

  private final String status;

  // ===== 라인 요청 DTO =====
  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class RequestOrderLineRequest {
    @NotNull
    private final Long itemId;
    @NotNull
    private final int qty;
  }
}
