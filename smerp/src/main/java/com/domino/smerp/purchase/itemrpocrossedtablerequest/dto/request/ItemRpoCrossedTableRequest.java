package com.domino.smerp.purchase.itemrpocrossedtablerequest.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRpoCrossedTableRequest {

  @NotNull
  private Long itemId;   // 품목 ID

  @Min(1)
  private int qty;       // 요청 수량
}
