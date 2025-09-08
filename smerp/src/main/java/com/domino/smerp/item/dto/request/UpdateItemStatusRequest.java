package com.domino.smerp.item.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateItemStatusRequest {

  private String itemAct;        // 품목 사용 여부
  private Integer safetyStock;   // 안전재고 수량
  private String safetyStockAct; // 안전재고 사용 여부

}

