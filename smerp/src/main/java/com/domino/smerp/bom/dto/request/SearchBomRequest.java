package com.domino.smerp.bom.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchBomRequest {
  private final String itemStatus;
  private final String itemName;
  private final String specification;

}
