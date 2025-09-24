package com.domino.smerp.bom.dto.response;

import com.domino.smerp.bom.entity.BomCostCache;
import java.math.BigDecimal;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BomCostCacheResponse { // 정전개, 역전개용

  private final Long itemId;
  private final String itemName;
  private final String itemStatus;
  private final BigDecimal qty;
  private final BigDecimal unitCost;
  private final BigDecimal totalCost;
  private final int depth;
  private final List<BomCostCacheResponse> children;

  public static BomCostCacheResponse of(final BomCostCache cache,
      final int depth,
      final BigDecimal totalCost,
      final List<BomCostCacheResponse> children) {
    return BomCostCacheResponse.builder()
        .itemId(cache.getChildItemId())
        .itemName(cache.getItemName())
        .itemStatus(cache.getItemStatus().getDescription())
        .qty(cache.getTotalQty())
        .unitCost(cache.getUnitCost())
        .totalCost(totalCost != null ? totalCost : cache.getTotalCost())
        .depth(depth)
        .children(children != null ? children : List.of())
        .build();
  }
}
