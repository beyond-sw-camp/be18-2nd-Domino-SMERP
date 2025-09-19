package com.domino.smerp.bom.dto.response;

import com.domino.smerp.bom.entity.BomCostCache;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BomCostResponse {

  private final Long itemId;
  private final String itemName;
  private final BigDecimal qty;
  private final BigDecimal unitCost;
  private final BigDecimal totalCost;
  private final int depth;
  private final List<BomCostResponse> children;

  public static BomCostResponse of(final BomCostCache cache,
      final int depth,
      final BigDecimal totalCost,
      final List<BomCostResponse> children) {
    return BomCostResponse.builder()
        .itemId(cache.getChildItemId())
        .itemName("TODO: itemName") // 필요하면 Item join 결과 넣기
        .qty(cache.getTotalQty())
        .unitCost(cache.getUnitCost())
        .totalCost(totalCost != null ? totalCost : cache.getTotalCost())
        .depth(depth)
        .children(children != null ? children : List.of())
        .build();
  }
}
