package com.domino.smerp.item.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
@Builder
@AllArgsConstructor
public class CreateItemRequest {

  @NotNull(message = "품목 구분을 선택해주세요")
  @Range(min = 1, max = 6, message = "존재하지 않는 품목 구분입니다.")
  private final Long itemStatusId;
  @NotNull(message = "품목명은 필수로 기입하셔야 합니다.")
  private final String name;
  private final String specification;
  @NotNull(message = "단위는 필수로 기입하셔야 합니다.")
  private final String unit;
  @DecimalMin(value = "0.0", inclusive = true, message = "단가는 0.0이상이어야 합니다.")
  private final BigDecimal inboundUnitPrice;
  @DecimalMin(value = "0.0", inclusive = true, message = "단가는 0.0이상이어야 합니다.")
  private final BigDecimal outboundUnitPrice;
  @NotNull(message = "품목 사용 여부를 선택해주세요")
  private final String itemAct;
  @NotNull(message = "안전 재고 수량은 필수로 기입하셔야 합니다.")
  @Min(value = 0, message = "안전 재고 수량은 0이상이어야 합니다.")
  private final Integer safetyStock;
  @NotNull(message = "안전 재고 사용여부를 선택해주세요")
  private final String safetyStockAct;
  @NotNull(message = "RFID는 필수로 기입하셔야 합니다.")
  private final String rfid;
  private final String groupName1;
  private final String groupName2;
  private final String groupName3;
}