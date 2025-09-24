package com.domino.smerp.workorder.dto.response;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class WorkOrderListResponse {
  @Builder.Default
  private List<WorkOrderResponse> workOrderResponses = new ArrayList<>();

}
