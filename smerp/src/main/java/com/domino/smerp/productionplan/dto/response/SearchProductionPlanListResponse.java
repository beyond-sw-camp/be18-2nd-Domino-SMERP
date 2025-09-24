package com.domino.smerp.productionplan.dto.response;

import com.domino.smerp.productionplan.constants.Status;
import com.domino.smerp.productionplan.ProductionPlan;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchProductionPlanListResponse {

  private final Long id;
  private final String title;
  private final String name; // 사용자 이름
  private final Status status;
  private final String remark;
  private final BigDecimal qty;
  private final String documentNo;
  private final boolean isDeleted;

  public static SearchProductionPlanListResponse fromEntity(ProductionPlan plan) {
    return SearchProductionPlanListResponse.builder()
        .id(plan.getId())
        .title(plan.getTitle())
        .status(plan.getStatus())
        .remark(plan.getRemark())
        .qty(plan.getQty())
        .documentNo(plan.getDocumentNo())
        .isDeleted(plan.isDeleted())
        .build();
  }
}
