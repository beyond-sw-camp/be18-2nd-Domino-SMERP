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

  @NotNull(message = "품목 구분은 필수입니다.")
  @Range(min = 1, max = 6, message = "존재하지 않는 품목 구분입니다.")
  private final Long itemStatusId;

  // Optional 수정 가능
  private final String name;
  private final String specification;
  private final String unit;

  @DecimalMin(value = "0.0", inclusive = true, message = "입고 단가는 0.0 이상이어야 합니다.")
  private final BigDecimal inboundUnitPrice;

  @DecimalMin(value = "0.0", inclusive = true, message = "출고 단가는 0.0 이상이어야 합니다.")
  private final BigDecimal outboundUnitPrice;

  private final String rfid;
  private final String groupName1;
  private final String groupName2;
  private final String groupName3;
}