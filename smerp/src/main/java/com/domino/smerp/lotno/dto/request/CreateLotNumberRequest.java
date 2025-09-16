package com.domino.smerp.lotno.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateLotNumberRequest {

  @NotNull(message = "품목코드를 입력해주세요")
  private final Long itemId;

  @NotNull
  private final Instant lotInstant;

  @NotNull(message = "수량을 입력해주세요")
  @DecimalMin("0.000")
  private final BigDecimal qty;

  @NotBlank(message = "사용상태를 설정해주세요")
  private final String status;

}
