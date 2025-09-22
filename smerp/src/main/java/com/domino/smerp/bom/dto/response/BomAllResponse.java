package com.domino.smerp.bom.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BomAllResponse {
  private final List<BomListResponse> inbound;       // 정전개
  private final List<BomListResponse> outbound;      // 역전개
  private final BomCostCacheResponse requirements;   // 원재료 리스트

}
