package com.domino.smerp.purchase.requestpurchaseorder.dto.request;

import com.domino.smerp.purchase.itemrpocrossedtablerequest.dto.request.ItemRpoCrossedTableRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * 구매요청 등록/수정 Request DTO
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestPurchaseOrderRequest {

  @NotNull
  private Long userId; // 사용자 ID

  @NotNull
  private Instant deliveryAt; // 납기 요청일시 (UTC)

  @Size(max = 100)
  private String remark; // 비고

  @NotNull
  private String status; // 상태 (pending, approved, completed, returned)

  @NotNull
  private List<ItemRpoCrossedTableRequest> items; // 교차테이블 요청 DTO
}
