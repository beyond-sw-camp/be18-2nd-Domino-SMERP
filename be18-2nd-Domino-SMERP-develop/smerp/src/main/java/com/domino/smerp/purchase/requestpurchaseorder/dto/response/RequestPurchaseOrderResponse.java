package com.domino.smerp.purchase.requestpurchaseorder.dto.response;

import com.domino.smerp.purchase.itemrpocrossedtable.dto.response.ItemRpoCrossedTableResponse;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구매요청 조회/상세 Response DTO
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestPurchaseOrderResponse {

  private Long rpoId; // 구매요청 PK
  private Long userId; // 사용자 ID
  private Instant createdAt; // 생성일시
  private Instant updatedAt; // 수정일시
  private Instant deliveryAt; // 납기일시
  private String remark; // 비고
  private String status; // 상태
  private List<ItemRpoCrossedTableResponse> items; // 품목 라인 리스트

  public static RequestPurchaseOrderResponse from(RequestPurchaseOrder entity) {
    return RequestPurchaseOrderResponse.builder()
        .rpoId(entity.getRpoId())
        .userId(entity.getUser().getUserId())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .deliveryAt(entity.getDeliveryAt())
        .remark(entity.getRemark())
        .status(entity.getStatus().name())
        .items(entity.getItems().stream()
            .map(ItemRpoCrossedTableResponse::from)
            .collect(Collectors.toList()))
        .build();
  }
}
