package com.domino.smerp.purchase.requestorder.dto.response;

import com.domino.smerp.purchase.requestorder.RequestOrder;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 발주(RequestOrder) 조회/상세 Response DTO
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestOrderResponse {

  private Long roId; // 발주 PK
  private Long userId; // 작성자 ID
  private Long clientId; // 거래처 ID
  private Long rpoId; // 구매요청 ID
  private Instant createdAt; // 생성일시
  private Instant updatedAt; // 수정일시
  private Instant deliveryAt; // 납기일시
  private String remark; // 비고
  private String status; // 상태
  private List<RequestOrderLineResponse> items; // 품목 라인 리스트

  public static RequestOrderResponse from(final RequestOrder entity) {
    return RequestOrderResponse.builder()
        .roId(entity.getRoId())
        .userId(entity.getUser().getUserId())
        .clientId(entity.getClient().getClientId())
        .rpoId(
            entity.getRequestPurchaseOrder() != null ? entity.getRequestPurchaseOrder().getRpoId()
                : null)
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .deliveryAt(entity.getDeliveryAt())
        .remark(entity.getRemark())
        .status(entity.getStatus().name())
        .items(entity.getItems().stream()
            .map(RequestOrderLineResponse::from)
            .collect(Collectors.toList()))
        .build();
  }

  // ====== 내부 DTO: 품목 라인 ======
  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class RequestOrderLineResponse {

    private Long itemRoId; // 품목-발주 교차테이블 PK
    private Long itemId;   // 품목 ID
    private int qty;       // 수량

    public static RequestOrderLineResponse from(
        final com.domino.smerp.purchase.requestorder.ItemRoCrossedTable entity) {
      return RequestOrderLineResponse.builder()
          .itemRoId(entity.getItemRoId())
          .itemId(entity.getItem().getItemId())
          .qty(entity.getQty())
          .build();
    }
  }
}
