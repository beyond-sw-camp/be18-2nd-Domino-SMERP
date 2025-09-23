package com.domino.smerp.bom.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BomListResponse {  // BOM 첫 화면 item띄워주기

  private Long itemId;          // 품목 PK
  private String itemName;      // 품목명
  private String specification;  // 규격
  private String itemStatus;    // 품목 구분
  private Long materialCount;   // 원재료 개수
  private boolean hasBom;       // BOM 등록 여부

}
