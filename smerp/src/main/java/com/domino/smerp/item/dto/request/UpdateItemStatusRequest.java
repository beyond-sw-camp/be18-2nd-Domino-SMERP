package com.domino.smerp.item.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateItemStatusRequest {

  private final String itemAct;        // 품목 사용 여부
  @Min(0)
  private final Integer safetyStock;   // 안전재고 수량
  private final String safetyStockAct; // 안전재고 사용 여부

}
