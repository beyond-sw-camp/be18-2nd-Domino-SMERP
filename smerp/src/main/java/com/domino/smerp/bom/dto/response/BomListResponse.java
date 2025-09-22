package com.domino.smerp.bom.dto.response;

import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.item.Item;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

  private String itemStatus;    // 품목 구분
  private Long itemId;          // 품목 FK
  private String itemName;      // 품목 명
  private String specification;  // 품목 규격
  private BigDecimal rawMaterialQty;       // 원재료 수량
  private List<BomListResponse> children; //  계층 구조 표현
  private Long bomId;           // bom id

  public static BomListResponse fromEntity(final Bom bom) {
    final Item item = bom.getChildItem();                  // 하위 품목 기준
    return BomListResponse.builder()
        .bomId(bom.getBomId())
        .itemId(item.getItemId())
        .itemName(item.getName())
        .specification(item.getSpecification())
        .itemStatus(item.getItemStatus().getStatus().getDescription())
        .rawMaterialQty(bom.getQty())
        .children(new ArrayList<>()) // 기본 리스트는 비어있습니다.
        .build();
  }

  // 자기 자신 기준
  public static BomListResponse fromSelf(final Item item) {
    return BomListResponse.builder()
        .bomId(null)
        .itemId(item.getItemId())
        .itemName(item.getName())
        .specification(item.getSpecification())
        .itemStatus(item.getItemStatus().getStatus().getDescription())
        .rawMaterialQty(BigDecimal.ONE)
        .children(new ArrayList<>())
        .build();
  }

}
