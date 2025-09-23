package com.domino.smerp.location.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationResponse {

  private final Long id;

  private final String rackNo;

  private final String levelNo;

  private final String binNo;

  private final boolean filled;

  //warehouse id x -> warehouse api용

}
