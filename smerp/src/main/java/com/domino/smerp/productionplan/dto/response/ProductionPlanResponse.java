package com.domino.smerp.productionplan.dto.response;

import com.domino.smerp.productionplan.constants.Status;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductionPlanResponse {

  @NotNull
  private final Long id;

  //사용자 이름
  private final String name;

  @NotNull
  private final Status status;

  private final String remark;

  private final String title;

  @NotNull
  private final String documentNo;

  @NotNull
  private final boolean isDeleted;

  @NotNull
  private final BigDecimal qty;
}
