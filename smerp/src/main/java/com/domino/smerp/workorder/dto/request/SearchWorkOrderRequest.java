package com.domino.smerp.workorder.dto.request;

import com.domino.smerp.workorder.constants.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchWorkOrderRequest {
  private final String itemName;
  private final Status status;
  private final String userName;
  private final Long productionPlanId;
}
