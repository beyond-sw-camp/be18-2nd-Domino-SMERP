package com.domino.smerp.domain.item.dto;

import com.domino.smerp.domain.item.enums.ItemAct;
import com.domino.smerp.domain.item.enums.SafetyStockAct;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemRequestDto {
  private Integer itemStatusId;
  private String name;
  private String specification;
  private String unit;
  private BigDecimal inboundUnitPrice;
  private BigDecimal outboundUnitPrice;
  private ItemAct itemAct;
  private Integer safetyStock;
  private SafetyStockAct safetyStockAct;
  private String rfid;
  private String groupName1;
  private String groupName2;
  private String groupName3;
}