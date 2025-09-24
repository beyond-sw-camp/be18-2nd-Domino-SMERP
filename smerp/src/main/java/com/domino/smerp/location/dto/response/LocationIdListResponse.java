package com.domino.smerp.location.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
//수정용
public class LocationIdListResponse {

  @Builder.Default //생성 후 바로 값 넣음 x
  private final List<Long> locationIds = new ArrayList<>();
}
