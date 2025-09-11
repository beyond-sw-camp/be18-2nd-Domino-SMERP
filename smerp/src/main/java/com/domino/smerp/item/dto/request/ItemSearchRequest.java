package com.domino.smerp.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemSearchRequest {

  private final String status;         // 품목 구분
  private final String name;           // 품목 명
  private final String specification;   // 품목 규격
  private final String groupName1;     // 품목 그룹1
  private final String groupName2;     // 품목 그룹2
  private final String groupName3;     // 품목 그룹3
}