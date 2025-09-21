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
public class CreateProductionPlanRequest {

  @NotNull(message = "제목 필수입니다")
  private final String title;

  @NotNull(message = "이름을 입력해주세요")
  private final String name;

  private final Status status;

  private final String remark;

  @NotNull
  private final BigDecimal qty;
}
