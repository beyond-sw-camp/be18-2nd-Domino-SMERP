package com.domino.smerp.bom.dto.response;

import com.domino.smerp.bom.entity.BomCostCache;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BomCostResponse {

  private Long itemId;             // 품목 ID
  private String itemName;         // 품목명 (조회 시 ItemService 등으로 보강)
  private BigDecimal totalQty;     // 누적 소요 수량
  private BigDecimal unitCost;     // 단가
  private BigDecimal totalCost;    // 누적 원가
  private List<BomCostResponse> children = new ArrayList<>();

  // 캐시 기반 변환
  public static BomCostResponse fromCache(final BomCostCache cache) {
    return BomCostResponse.builder()
        .itemId(cache.getChildItemId())
        .totalQty(cache.getTotalQty())
        .unitCost(cache.getUnitCost())
        .totalCost(cache.getTotalCost())
        .children(new ArrayList<>())
        .build();
  }
}
