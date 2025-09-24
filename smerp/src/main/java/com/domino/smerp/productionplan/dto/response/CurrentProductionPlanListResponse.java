package com.domino.smerp.productionplan.dto.response;

import com.domino.smerp.productionplan.constants.Status;
import com.domino.smerp.productionplan.ProductionPlan;
import com.domino.smerp.productionplan.dto.response.ProductionPlanListResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrentProductionPlanListResponse {

  private final Long id;
  private final String title;
  private final String name; // 사용자 이름
  private final Status status;
  private final String remark;
  private final BigDecimal qty;
  private final String documentNo;

  public static CurrentProductionPlanListResponse fromEntity(ProductionPlan plan) {
    return CurrentProductionPlanListResponse.builder()
        .id(plan.getId())
        .title(plan.getTitle())
        .status(plan.getStatus())
        .remark(plan.getRemark())
        .qty(plan.getQty())
        .documentNo(plan.getDocumentNo())
        .build();
  }
}
