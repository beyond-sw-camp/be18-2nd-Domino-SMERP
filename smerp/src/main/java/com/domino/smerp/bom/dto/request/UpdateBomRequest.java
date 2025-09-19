package com.domino.smerp.bom.dto.request;


import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UpdateBomRequest {

  @Positive(message = "수량은 0보다 커야 합니다.")
  private BigDecimal qty;

  private String remark;

}
