package com.domino.smerp.lotno.constants;

import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import java.util.Arrays;

public enum LotNumberStatus {
  ACTIVE("사용중"),
  SHIPPED("출고완"),
  DEFECTIVE("불량"),
  RETURNED("반품");

  private final String description;

  LotNumberStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  // 한글명 또는 Enum 이름(대소문자 무관)으로 Enum 찾기
  public static LotNumberStatus fromLabel(String label) {
    return Arrays.stream(values())
        .filter(status -> status.description.equals(label)   // 한글 설명 매칭
            || status.name().equalsIgnoreCase(label))                   // Enum 이름 매칭
        .findFirst()
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_LOTNUMBER_STATUS));
  }
}
