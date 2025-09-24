package com.domino.smerp.productionresult.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductionResultListResponse {
  @Builder.Default
  private final List<ProductionResultResponse> productionResultResponses = new ArrayList<>();

  private final BigDecimal totalQty;
}
