package com.domino.smerp.lotno.dto.response;

import com.domino.smerp.lotno.LotNumber;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LotNumberDetailResponse {

  // Lot.no
  private Long lotId;
  private String lotName;     // Lot.No 번호
  private Long itemId;
  private String itemName;    // 품목명
  private String status;      // Lot.No 사용상태
  private BigDecimal lotQty;  // Lot.No 초기수량

  // TODO: 생산, 재고, 재고수불부 필드 추후 추가

  // 생산
  // private Instant acEndAt;                  // 생산일자
  // private String userName;                  // 생산 담당자
  // private BigDecimal productionQty;         // 생산수량
  // private List<Object> defectiveProduct;    // 해당 Lot.No 불량 목록

  // 재고
  // private BigDecimal stockQty; // 재고수량
  // private List<WarehouseListResponse> warehouseName; // 창고명 들
  // private List<LocationListResponse> location; // 위치들

  // 재고수불부
  // private List<StockMovementListResponse> stockMovement;

  public static LotNumberDetailResponse fromEntity(final LotNumber lotNumber) {
    return LotNumberDetailResponse.builder()
        .lotId(lotNumber.getLotId())
        .lotName(lotNumber.getName())
        .itemId(lotNumber.getItem().getItemId())
        .itemName(lotNumber.getItem().getName())
        .lotQty(lotNumber.getQty())
        .status(lotNumber.getStatus().getDescription())
        .build();
  }


}
