package com.domino.smerp.bom.dto.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateBomRequest {

  private final Long parentBomId;
  private Long parentItemId;
  private Long childItemId;
  private BigDecimal qty;
  private String remark;
}
