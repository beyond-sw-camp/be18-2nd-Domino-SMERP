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
public class CurrentWorkOrderListResponse {

  @Builder.Default
  List<CurrentWorkOrderResponse> currentWorkOrderResponses = new ArrayList<>();
}
