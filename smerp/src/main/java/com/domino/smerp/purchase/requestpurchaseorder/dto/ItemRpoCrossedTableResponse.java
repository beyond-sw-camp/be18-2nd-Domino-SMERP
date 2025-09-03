package com.domino.smerp.purchase.requestpurchaseorder.dto;

import com.domino.smerp.purchase.requestpurchaseorder.ItemRpoCrossedTable;

public record ItemRpoCrossedTableResponse(
    Long rpoId,
    Long itemId,
    int qty
) {

  public static ItemRpoCrossedTableResponse from(ItemRpoCrossedTable e) {
    return new ItemRpoCrossedTableResponse(
        e.getId().getRpoId(),
        e.getId().getItemId(),
        e.getQty()
    );
  }
}
