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
public class CreateProductionResultRequest {

  //담당자
  @NotNull
  private final String userName;

  //일자 no
  @NotNull
  private final String documentNo;

  //생산 공장
  @NotNull
  private final String factoryName;

  //받는 창고 -> 위치 할당 필요하므로 내부에서 결정
  //생산실적 하나 당 재고 여럿 발생 가능하므로 도착 창고를 지정할 수 없음

  //실제 생산일 -> 작업지시로 저장
  private final Instant producedAt;

  //품목명 -> 규격도 품목으로 있는 정보
  @NotNull
  private final String itemName;

  //수량
  @NotNull
  private final BigDecimal qty;

  //작업지시서
  @NotNull
  private final Long workOrderId;

  private final String remark;
}
