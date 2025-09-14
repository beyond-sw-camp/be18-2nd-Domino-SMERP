package com.domino.smerp.lotno.dto.response;


import com.domino.smerp.lotno.entity.LotNumber;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LotNumberListResponse {

  // TODO: Lot.No, 품목명, 생산일자, 입고수량, 출고수량, 재고수량, 공정흐름
  private String lotName;      // Lot 명
  private String itemName;     // 품목명
  private BigDecimal qty;      // 수량
  private String status;       // Lot.No 상태 (enum label)

  public static LotNumberListResponse fromEntity(LotNumber lotNumber) {

    return LotNumberListResponse.builder()
        .lotName(lotNumber.getName())
        .itemName(lotNumber.getItem().getName())
        .qty(lotNumber.getQty())
        .status(lotNumber.getStatus().getDescription())
        .build();
  }

}
