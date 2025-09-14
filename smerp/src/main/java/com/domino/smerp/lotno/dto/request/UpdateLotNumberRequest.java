package com.domino.smerp.lotno.dto.request;


import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateLotNumberRequest {

  private final BigDecimal qty;
  private final String status;

}
