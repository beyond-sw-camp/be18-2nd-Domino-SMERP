package com.domino.smerp.bom.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateBomRequest {

  @NotNull(message = "부모 품목 ID는 필수입니다.")
  private Long parentItemId;

  @NotNull(message = "자식 품목 ID는 필수입니다.")
  private Long childItemId;

  @NotNull(message = "수량은 필수입니다.")
  @Positive(message = "수량은 0보다 커야 합니다.") // @Min(0)은 0이상, @Positive는 0초과
  private BigDecimal qty;

  private String remark;

}
