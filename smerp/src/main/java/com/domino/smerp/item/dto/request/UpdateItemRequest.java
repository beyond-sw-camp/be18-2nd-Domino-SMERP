package com.domino.smerp.item.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
@Builder
@AllArgsConstructor
public class UpdateItemRequest {

  @NotNull
  @Range(min = 1, max = 6)
  private final Long itemStatusId;
  private final String name;
  private final String specification;
  private final String unit;
  @DecimalMin(value = "0.0", inclusive = true)
  private final BigDecimal inboundUnitPrice;
  @DecimalMin(value = "0.0", inclusive = true)
  private final BigDecimal outboundUnitPrice;
  private final String rfid;
  private final String groupName1;
  private final String groupName2;
  private final String groupName3;
}