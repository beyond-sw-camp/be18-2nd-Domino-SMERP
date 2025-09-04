package com.domino.smerp.item.constants;

import java.util.Arrays;

public enum ItemStatusStatus {
  RAW_MATERIAL("원재료"),
  AUXILIARY_MATERIAL("부재료"),
  SEMI_FINISHED_PRODUCT("반제품"),
  FINISHED_PRODUCT("제품"),
  MERCHANDISE("상품"),
  INTANGIBLE_GOODS("무형상품");

  private final String description;

  ItemStatusStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  // 한글명 또는 Enum 이름(대소문자 무관)으로 Enum 찾기
  public static ItemStatusStatus fromLabel(String label) {
    return Arrays.stream(values())
        .filter(status -> status.description.equals(label)   // 한글 설명 매칭
            || status.name().equalsIgnoreCase(label))   // Enum 이름 매칭
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("잘못된 상태: " + label));
  }
}
