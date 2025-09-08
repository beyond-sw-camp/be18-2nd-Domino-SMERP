package com.domino.smerp.purchase.requestorder.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 발주(RequestOrder) 등록/수정 Request DTO
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestOrderRequest {

  @NotNull
  private Long userId; // 작성자 ID

  @NotNull
  private Long clientId; // 거래처 ID

  private Long rpoId; // 구매요청 ID (없을 수도 있음)

  @NotNull
  private Instant deliveryAt; // 납기 요청일시 (UTC)

  @Size(max = 100)
  private String remark; // 비고

  @NotNull
  private String status; // 상태 (pending, approved, completed, returned)

  @NotNull
  private List<RequestOrderLineRequest> items; // 품목 라인 리스트

  // ====== 내부 DTO: 품목 라인 ======
  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class RequestOrderLineRequest {

    @NotNull
    private Long itemId; // 품목 ID

    @Min(1)
    private int qty; // 수량
  }
}
