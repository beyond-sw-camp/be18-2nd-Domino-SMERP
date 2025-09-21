package com.domino.smerp.productionplan.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//주문 품목 생성 시에 반환
@Getter
@RequiredArgsConstructor
public class ItemOrderCreatedEvent {
  private final Long itemOrderId;
}
