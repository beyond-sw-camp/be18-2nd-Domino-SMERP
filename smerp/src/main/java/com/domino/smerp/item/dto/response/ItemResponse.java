package com.domino.smerp.item.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class ItemResponse {

  private Long itemId;
  private Long itemStatusId;
  private String itemStatusName;
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

}