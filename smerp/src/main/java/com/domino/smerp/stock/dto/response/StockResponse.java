package com.domino.smerp.stock.dto.response;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StockResponse {
  //품목 코드 -> id로 사용
  @NotNull
  private final Long itemId;

  //품목명
  @NotNull
  private final String itemName;

  //규격
  private final String specification;

  //수량 - 매 회차마다 추가되는 값
  @NotNull
  private final BigDecimal currentQty;


}
