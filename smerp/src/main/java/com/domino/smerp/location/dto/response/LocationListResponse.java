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
//생성, 수정 시 보여줄 위치들
public class LocationListResponse {

  @Builder.Default
  private final List<LocationResponse> locationResponses = new ArrayList<>();

}
