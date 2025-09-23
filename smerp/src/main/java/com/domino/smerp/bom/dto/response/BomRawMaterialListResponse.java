package com.domino.smerp.bom.dto.response;


import com.domino.smerp.bom.entity.BomCostCache;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BomRawMaterialListResponse {   // BOM 원재료 리스트
  private Long itemId;        // 품목 ID
  private String itemName;    // 품목명
  private BigDecimal qty;     // 소요량
  private BigDecimal unitCost; // 단가
  private BigDecimal totalCost; // 총 원가
  private String itemStatus;    // 품목 구분

  public static BomRawMaterialListResponse fromCache(final BomCostCache cache) {
    return BomRawMaterialListResponse.builder()
        .itemId(cache.getChildItemId())
        .itemName(cache.getItemName())
        .qty(cache.getTotalQty())
        .unitCost(cache.getUnitCost())
        .totalCost(cache.getTotalCost())
        .itemStatus(cache.getItemStatus().getDescription())
        .build();
  }

}
