package com.domino.smerp.purchase.requestpurchaseorder.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemRpoCrossedTableRequest(
    @NotNull Long itemId,
    @Min(1) int qty
) {

}
