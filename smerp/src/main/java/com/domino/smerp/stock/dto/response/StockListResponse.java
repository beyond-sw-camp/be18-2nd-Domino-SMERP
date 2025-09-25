package com.domino.smerp.stock.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StockListResponse {

  @Builder.Default
  List<StockResponse> stockResponses = new ArrayList<>();
}
