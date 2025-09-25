package com.domino.smerp.stock.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
//생산계획 생성용
@AllArgsConstructor
@Getter
public class StockBelowSafetyEvent {
  private final Long itemId;
}
