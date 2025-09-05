package com.domino.smerp.purchase.itemrpocrossedtablerequest.dto.response;

import com.domino.smerp.purchase.itemrpocrossedtablerequest.ItemRpoCrossedTable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRpoCrossedTableResponse {

  private Long itemRpoId;   // 교차테이블 PK
  private Long rpoId;       // 구매요청 ID
  private Long itemId;      // 품목 ID
  private int qty;          // 요청 수량

  public static ItemRpoCrossedTableResponse from(final ItemRpoCrossedTable entity) {
    return ItemRpoCrossedTableResponse.builder()
        .itemRpoId(entity.getItemRpoId())
        .rpoId(entity.getRequestPurchaseOrder().getRpoId())
        .itemId(entity.getItem().getItemId())
        .qty(entity.getQty())
        .build();
  }
}
