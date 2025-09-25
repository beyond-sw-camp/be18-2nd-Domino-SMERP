package com.domino.smerp.stock.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

//작업 지시 생성용
@AllArgsConstructor
@Getter
public class StockAboveSafetyEvent {
  public final Long itemId;
}
