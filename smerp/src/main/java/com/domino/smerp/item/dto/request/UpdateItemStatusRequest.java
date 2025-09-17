package com.domino.smerp.item.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateItemStatusRequest {


  @NotNull(message = "품목 사용 여부는 필수입니다.")
  private final String itemAct;

  @NotNull(message = "안전 재고 수량은 필수입니다.")
  @Min(value = 0, message = "안전 재고 수량은 0 이상이어야 합니다.")
  private final BigDecimal safetyStock;

  @NotNull(message = "안전 재고 사용 여부는 필수입니다.")
  private final String safetyStockAct;
}
