package com.domino.smerp.purchase.requestorder.dto.response;

import com.domino.smerp.item.Item;
import com.domino.smerp.purchase.itemrocrossedtable.ItemRoCrossedTable;
import com.domino.smerp.purchase.requestorder.RequestOrder;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestOrderResponse {

  private Long roId;
  private Long userId;
  private Long clientId;
  private Long rpoId;
  private Instant createdDate;
  private Instant updatedDate;
  private Instant deliveryDate;
  private String remark;
  private String status;

  private List<RequestOrderLineResponse> lines;

  public static RequestOrderResponse from(final RequestOrder entity,
      final List<ItemRoCrossedTable> items) {
    return RequestOrderResponse.builder()
        .roId(entity.getRoId())
        .userId(entity.getUser().getUserId())
        .clientId(entity.getClient().getClientId())
        .rpoId(
            entity.getRequestPurchaseOrder() != null ? entity.getRequestPurchaseOrder().getRpoId()
                : null)
        .createdDate(entity.getCreatedDate())
        .updatedDate(entity.getUpdatedDate())
        .deliveryDate(entity.getDeliveryDate())
        .remark(entity.getRemark())
        .status(entity.getStatus().name())
        .lines(items.stream().map(RequestOrderLineResponse::from).toList())
        .build();
  }

  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class RequestOrderLineResponse {

    private Long lineId;
    private Long itemId;
    private String itemName;
    private int qty;

    public static RequestOrderLineResponse from(final ItemRoCrossedTable entity) {
      Item item = entity.getItem();
      return RequestOrderLineResponse.builder()
          .lineId(entity.getItemRoId())
          .itemId(item.getItemId())
          .itemName(item.getName())
          .qty(entity.getQty())
          .build();
    }
  }
}
