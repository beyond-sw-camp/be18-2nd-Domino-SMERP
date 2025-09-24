package com.domino.smerp.productionplan.dto.request;

import com.domino.smerp.productionplan.constants.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchProductionPlanRequest {

  private final String title;      // 생산계획 제목
  private final Status status;     // 생산계획 상태
}