package com.domino.smerp.productionplan.dto;

import com.domino.smerp.productionplan.constants.Status;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateProductionPlanRequest {


  private final String title;

  private final String name;

  private final Status status;

  private final String remark;

  private final String documentNo;

  private final BigDecimal qty;
}
