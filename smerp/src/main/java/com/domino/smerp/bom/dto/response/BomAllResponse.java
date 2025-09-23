package com.domino.smerp.bom.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BomAllResponse {

  private final BomCostCacheResponse inbound;                // 정전개 (트리)
  private final List<BomCostCacheResponse> outbound;         // 역전개 (여러 조상 가능)
  private final List<BomRawMaterialListResponse> rawMaterials; // 원재료 flat list

}
