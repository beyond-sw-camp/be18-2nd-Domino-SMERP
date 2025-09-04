package com.domino.smerp.item.dto.request;

import com.domino.smerp.item.constants.ItemAct;
import com.domino.smerp.item.constants.SafetyStockAct;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequest {
  private Long itemStatusId;
  private String name;
  private String specification;
  private String unit;
  private BigDecimal inboundUnitPrice;
  private BigDecimal outboundUnitPrice;
  private String itemAct;
  private Integer safetyStock;
  private String safetyStockAct;
  private String rfid;
  private String groupName1;
  private String groupName2;
  private String groupName3;
}