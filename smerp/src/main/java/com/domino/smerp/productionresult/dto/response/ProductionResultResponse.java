package com.domino.smerp.productionresult.dto.response;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductionResultResponse {

  @NotNull
  private final Long id;

  //일자 no
  @NotNull
  private final String documentNo;

  //생산 공장
  @NotNull
  private final String factoryName;

  //받는 창고
  @NotNull
  private final String arriveWarehouseName;

  //품목명
  @NotNull
  private final String itemName;

  //규격
  @NotNull
  private final String specification;

  //수량
  @NotNull
  private final BigDecimal qty;

  //작업지시서 no
  @NotNull
  private final Long workOrderId;

  private final String remark;

}
