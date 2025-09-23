package com.domino.smerp.lotno.dto.response;


import com.domino.smerp.lotno.LotNumber;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LotNumberSimpleResponse {

  private Long itemId;
  private String itemName;
  private Long lotId;
  private String lotName;
  private BigDecimal qty;
  private String status;

  public static LotNumberSimpleResponse fromEntity(final LotNumber lotNubmer) {
    return LotNumberSimpleResponse.builder()
        .itemId(lotNubmer.getItem().getItemId())
        .itemName(lotNubmer.getItem().getName())
        .lotId(lotNubmer.getLotId())
        .lotName(lotNubmer.getName())
        .qty(lotNubmer.getQty())
        .status(lotNubmer.getStatus().getDescription())
        .build();
  }

}
