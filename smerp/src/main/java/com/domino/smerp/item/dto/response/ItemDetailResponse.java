package com.domino.smerp.item.dto.response;

import com.domino.smerp.item.Item;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemDetailResponse {

  private final Long itemId;
  private final Long itemStatusId;
  private final String itemStatusName;
  private final String name;
  private final String specification;
  private final String unit;
  private final BigDecimal inboundUnitPrice;
  private final BigDecimal outboundUnitPrice;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
  private final Instant createdAt;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
  private final Instant updatedAt;
  private final String itemAct;
  private final Integer safetyStock;
  private final String safetyStockAct;
  private final String rfid;
  private final String groupName1;
  private final String groupName2;
  private final String groupName3;

  public static ItemDetailResponse fromEntity(Item item) {
    return ItemDetailResponse.builder()
        .itemId(item.getItemId())
        .itemStatusId(item.getItemStatus().getItemStatusId())
        .itemStatusName(item.getItemStatus().getStatus().getDescription())
        .name(item.getName())
        .specification(item.getSpecification())
        .unit(item.getUnit())
        .inboundUnitPrice(item.getInboundUnitPrice())
        .outboundUnitPrice(item.getOutboundUnitPrice())
        .createdAt(item.getCreatedAt())
        .updatedAt(item.getUpdatedAt())
        .itemAct(item.getItemAct().getDescription())
        .safetyStock(item.getSafetyStock())
        .safetyStockAct(item.getSafetyStockAct().getDescription())
        .rfid(item.getRfid())
        .groupName1(item.getGroupName1())
        .groupName2(item.getGroupName2())
        .groupName3(item.getGroupName3())
        .build();
  }
}