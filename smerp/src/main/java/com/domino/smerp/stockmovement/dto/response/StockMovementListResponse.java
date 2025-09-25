package com.domino.smerp.stockmovement.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StockMovementListResponse {

  List<StockMovementResponse> stockMovementResponses;
}
