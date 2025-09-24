package com.domino.smerp.productionplan.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//품목에 대한 bom 생성 시 반환
@Getter
@RequiredArgsConstructor
public class BOMReadyEvent {
  private final Long ProductionPlanId;
}
