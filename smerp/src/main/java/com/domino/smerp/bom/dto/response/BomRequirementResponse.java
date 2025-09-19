package com.domino.smerp.bom.dto.response;

import com.domino.smerp.bom.entity.BomCostCache;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BomRequirementResponse {

  private Long childItemId;   // 자식 품목 ID
  private String childItemName; // 자식 품목 이름
  private BigDecimal totalQty; // 누적 소요량
  private BigDecimal unitCost; // 단가
  private BigDecimal totalCost; // 총 원가
  private Integer depth;        // 계층 깊이

  public static BomRequirementResponse fromEntity(final BomCostCache cache, final String childItemName) {
    return BomRequirementResponse.builder()
        .childItemId(cache.getChildItemId())
        .childItemName(childItemName)
        .totalQty(cache.getTotalQty())
        .unitCost(cache.getUnitCost())
        .totalCost(cache.getTotalCost())
        .depth(cache.getDepth())
        .build();
  }
}
