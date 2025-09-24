package com.domino.smerp.lotno.dto.request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateLotNumberRequest {

  @DecimalMin("0.000")
  private final BigDecimal qty;
  private final String status;

}
