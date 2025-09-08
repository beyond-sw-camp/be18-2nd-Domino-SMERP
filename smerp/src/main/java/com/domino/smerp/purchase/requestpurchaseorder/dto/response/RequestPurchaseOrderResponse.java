package com.domino.smerp.purchase.requestpurchaseorder.dto.response;

import com.domino.smerp.item.Item;
import com.domino.smerp.purchase.itemrpocrossedtable.ItemRpoCrossedTable;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구매요청(RequestPurchaseOrder) Response DTO
 */
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestPurchaseOrderResponse {

  private Long rpoId;
  private Long userId;
  private Instant createdDate;
  private Instant updatedDate;
  private Instant deliveryDate;
  private String remark;
  private String status;
  private String documentNo;
  private boolean isDeleted;
  private Instant deletedAt;

  private List<RequestPurchaseOrderLineResponse> lines;

  public static RequestPurchaseOrderResponse from(
      final RequestPurchaseOrder entity,
      final List<ItemRpoCrossedTable> items
  ) {
    return RequestPurchaseOrderResponse.builder()
        .rpoId(entity.getRpoId())
        .userId(entity.getUser().getUserId())
        .createdDate(entity.getCreatedDate())
        .updatedDate(entity.getUpdatedDate())
        .deliveryDate(entity.getDeliveryDate())
        .remark(entity.getRemark())
        .status(entity.getStatus().name())
        .documentNo(entity.getDocumentNo())
        .isDeleted(entity.isDeleted())
        .deletedAt(entity.getDeletedAt())
        .lines(items.stream().map(RequestPurchaseOrderLineResponse::from).toList())
        .build();
  }

  // ===== 라인 응답 DTO =====
  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class RequestPurchaseOrderLineResponse {

    private Long lineId;
    private Long itemId;
    private String itemName;
    private int qty;

    public static RequestPurchaseOrderLineResponse from(final ItemRpoCrossedTable entity) {
      Item item = entity.getItem();
      return RequestPurchaseOrderLineResponse.builder()
          .lineId(entity.getItemRpoId())
          .itemId(item.getItemId())
          .itemName(item.getName())
          .qty(entity.getQty())
          .build();
    }
  }
}
