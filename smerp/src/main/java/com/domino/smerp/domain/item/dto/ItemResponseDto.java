package com.domino.smerp.domain.item.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemResponseDto {
  private Integer itemId;
  private Integer itemStatusId;
  private String name;
  private String specification;
  private String unit;
  private BigDecimal inboundUnitPrice;
  private BigDecimal outboundUnitPrice;
  private LocalDate createdDate;
  private LocalDate updatedDate;
  private String itemAct;
  private Integer safetyStock;
  private String safetyStockAct;
  private String rfid;
  private String groupName1;
  private String groupName2;
  private String groupName3;

  @Builder
  public ItemResponseDto(Integer itemId, Integer itemStatusId, String name, String specification, String unit, BigDecimal inboundUnitPrice, BigDecimal outboundUnitPrice, LocalDate createdDate, LocalDate updatedDate, String itemAct, Integer safetyStock, String safetyStockAct, String rfid, String groupName1, String groupName2, String groupName3) {
    this.itemId = itemId;
    this.itemStatusId = itemStatusId;
    this.name = name;
    this.specification = specification;
    this.unit = unit;
    this.inboundUnitPrice = inboundUnitPrice;
    this.outboundUnitPrice = outboundUnitPrice;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
    this.itemAct = itemAct;
    this.safetyStock = safetyStock;
    this.safetyStockAct = safetyStockAct;
    this.rfid = rfid;
    this.groupName1 = groupName1;
    this.groupName2 = groupName2;
    this.groupName3 = groupName3;
  }
}