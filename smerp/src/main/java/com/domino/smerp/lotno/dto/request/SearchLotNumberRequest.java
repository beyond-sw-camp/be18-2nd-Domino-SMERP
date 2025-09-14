package com.domino.smerp.lotno.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchLotNumberRequest {

  private final String lotName;        // Lot.No 명
  private final String itemName;       // 품목 명
  private final String status;         // Lot.No 사용상태
}
