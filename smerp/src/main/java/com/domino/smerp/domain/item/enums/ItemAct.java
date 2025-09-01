package com.domino.smerp.domain.item.enums;

public enum ItemAct {
  ACTIVE("사용중"),
  INACTIVE("사용중지");

  private final String description;
  ItemAct(String description) {
    this.description = description;
  }
  public String getDescription() { return this.description; }
}