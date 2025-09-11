package com.domino.smerp.lotno.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LotNumberSearchRequest {

  private final Long itemId;           // 품목 명
  private final String name;           // Lot.No 명
  private final String status;         // Lot.No 사용상태
}
