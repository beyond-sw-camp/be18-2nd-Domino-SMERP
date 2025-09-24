package com.domino.smerp.productionresult.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class UpdateProductionResultRequest {
  //담당자
  private final String userName;

  //일자 no
  @NotNull
  private final String documentNo;

  //생산 공장
  private final String factoryName;

  //받는 창고 -> 위치 할당 필요하므로 내부 책임으로

  //품목명
  private final String itemName;

  //실제 생산일 -> 작업지시로 저장
  private final Instant producedAt;

  //규격 -> item 속성이므로 불가
  //private final String specification;

  //수량
  private final BigDecimal qty;

  //작업지시서
  private final Long workOrderId;

}
