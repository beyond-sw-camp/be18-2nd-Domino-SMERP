package com.domino.smerp.stock.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UpdateStockRequest {
  //품목 코드 -> id로 사용
  private final Long itemId;

  //품목명
  private final String itemName;

  //규격 -> item 속성이므로 수정 불가

  //수량
  private final BigDecimal qty;

}
