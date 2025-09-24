package com.domino.smerp.productionplan.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductionPlanListResponse {
  @Builder.Default
  private List<ProductionPlanResponse> productionPlans = new ArrayList<>();
}
