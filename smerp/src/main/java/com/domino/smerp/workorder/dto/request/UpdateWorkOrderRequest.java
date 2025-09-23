package com.domino.smerp.workorder.dto.request;

import com.domino.smerp.workorder.constants.Status;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateWorkOrderRequest {

  //품목명
  private final String itemName;

  //생산계획 id
  private final Long productionPlanId;

  //상태
  private final Status status;

  //계획수량
  private final BigDecimal planQty;

  //생산수량
  private final BigDecimal producedQty;

  //계획된 작업일
  private final Instant planAt;

  //실제 작업일
  private final Instant producedAt;

  private final String factoryName;

  //생산 담당자
  private final String userName;

  private final String remark;
}
