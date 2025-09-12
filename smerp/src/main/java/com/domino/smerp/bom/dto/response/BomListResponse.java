package com.domino.smerp.bom.dto.response;

import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.item.entity.Item;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BomListResponse {
  private Long bomId;           // bom id
  private Long itemId;          // 품목 FK
  private String itemName;      // 품목 명
  private String specification;  // 품목 규격
  private String itemStatus;    // 품목 구분
  private BigDecimal qty;       // 원재료 수량

  public static BomListResponse fromEntity(Bom bom) {
    Item item = bom.getChildItem();                  // 하위 품목 기준
    return BomListResponse.builder()
        .bomId(bom.getBomId())
        .itemId(item.getItemId())
        .itemName(item.getName())
        .specification(item.getSpecification())
        .itemStatus(item.getItemStatus().getStatus().getDescription())
        .qty(bom.getQty())
        .build();
  }

}
