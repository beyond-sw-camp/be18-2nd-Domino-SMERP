package com.domino.smerp.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSearchRequest {

  private String status;         // 품목 구분
  private String name;           // 품목 명
  private String specification;   // 품목 규격
  private String groupName1;     // 품목 그룹1
  private String groupName2;     // 품목 그룹2
  private String groupName3;     // 품목 그룹3
}