package com.domino.smerp.bom.dto.request;


import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UpdateBomRequest {
  private BigDecimal qty;
  private String remark;
  private Long ParentItemId;

}
