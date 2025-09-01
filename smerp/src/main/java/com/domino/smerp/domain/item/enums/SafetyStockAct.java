package com.domino.smerp.domain.item.enums;

public enum SafetyStockAct {
  ENABLED("사용중"),
  DISABLED("사용안함");

  private final String description;

  SafetyStockAct(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }
}