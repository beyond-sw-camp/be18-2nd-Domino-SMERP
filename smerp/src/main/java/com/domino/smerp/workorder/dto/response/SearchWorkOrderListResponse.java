package com.domino.smerp.workorder.dto.response;

import com.domino.smerp.client.Client;
import com.domino.smerp.user.User;
import com.domino.smerp.workorder.WorkOrder;
import com.domino.smerp.workorder.constants.Status;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class SearchWorkOrderListResponse {

  private final Long id;

  //일자 no
  @NotNull
  private final String documentNo;

  //거래처명
  private final String companyName;

  //생산 담당자
  private final String userName;

  //품목명
  private final String itemName;

  //상태
  @NotNull
  private final Status status;

  //계획수량
  @NotNull
  private final BigDecimal planQty;

  //생산수량
  @Builder.Default
  private final BigDecimal producedQty = BigDecimal.ZERO;

  //계획된 작업일
  private final Instant planAt;

  public static SearchWorkOrderListResponse fromEntity(WorkOrder workOrder) {

    /*
    // 생산 수량 (없으면 0)
    BigDecimal producedQty = BigDecimal.ZERO;
    if (workOrder.getProductionResult() != null) {
      producedQty = workOrder.getProductionResult().getQty();
    }

     */

    // 생산 담당자
    User user = (workOrder.getProductionPlan() != null) ? workOrder.getProductionPlan().getUser() : null;
    String userName = (user != null) ? user.getName() : null;

    // 거래처
    Client client = (user != null) ? user.getClient() : null;
    String companyName = (client != null) ? client.getCompanyName() : null;

    // 품목명
    String itemName = (workOrder.getItem() != null) ? workOrder.getItem().getName() : null;

    return SearchWorkOrderListResponse.builder()
        .id(workOrder.getId())
        .companyName(companyName)
        .userName(userName)
        .documentNo(workOrder.getDocumentNo())
        .itemName(itemName)
        .status(workOrder.getStatus())
        .planQty(workOrder.getQty())
        //.producedQty(producedQty)
        .planAt(workOrder.getPlanAt())
        .build();
  }


}
