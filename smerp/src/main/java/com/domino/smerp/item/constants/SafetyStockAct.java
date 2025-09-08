package com.domino.smerp.item.constants;

import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import java.util.Arrays;

public enum SafetyStockAct {
  ENABLED("사용중"),
  DISABLED("사용안함");

  private final String description;

  SafetyStockAct(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public static SafetyStockAct fromLabel(String label) {
    return Arrays.stream(values())
        .filter(act -> act.description.equals(label) || act.name().equalsIgnoreCase(label))
        .findFirst()
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_SAFETY_STOCK_ACT));
  }
}