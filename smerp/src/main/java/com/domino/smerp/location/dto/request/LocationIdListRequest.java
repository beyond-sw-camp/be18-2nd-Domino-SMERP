package com.domino.smerp.location.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationIdListRequest {

  //재고 생성, 수정용 fill 대상
  @Builder.Default
  private final List<Long> locationIds = new ArrayList<>();
}
