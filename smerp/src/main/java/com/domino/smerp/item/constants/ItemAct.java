package com.domino.smerp.item.constants;

import java.util.Arrays;


public enum ItemAct {
  ACTIVE("사용중"),
  INACTIVE("사용중지");

  private final String description;

  ItemAct(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public static ItemAct fromLabel(String label) {
    return Arrays.stream(values())
        .filter(act -> act.description.equals(label) || act.name().equalsIgnoreCase(label))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("잘못된 상태: " + label));
  }
}