package com.domino.smerp.item.dto.response;

import com.domino.smerp.item.Item;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemStatusResponse {

  private final String itemAct;        // 품목 사용 여부
  private final BigDecimal safetyStock;
  private final String safetyStockAct; // 안전재고 사용 여부

  public static ItemStatusResponse fromEntity(Item item) {
    return ItemStatusResponse.builder()
        .itemAct(item.getItemAct().getDescription())
        .safetyStock(item.getSafetyStock())
        .safetyStockAct(item.getSafetyStockAct().getDescription())
        .build();
  }
}