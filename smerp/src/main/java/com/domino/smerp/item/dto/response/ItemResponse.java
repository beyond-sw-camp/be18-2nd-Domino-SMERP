package com.domino.smerp.item.dto.response;

import com.domino.smerp.item.Item;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ItemResponse {

  private Long itemId;
  private Long itemStatusId;
  private String itemStatusName;
  private String name;
  private String specification;
  private String unit;
  private BigDecimal inboundUnitPrice;
  private BigDecimal outboundUnitPrice;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
  private Instant createdDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
  private Instant updatedDate;
  private String itemAct;
  private Integer safetyStock;
  private String safetyStockAct;
  private String rfid;
  private String groupName1;
  private String groupName2;
  private String groupName3;

  // 정적 팩토리 메서드 (Entity → DTO)
  public static ItemResponse fromEntity(Item item) {
    return ItemResponse.builder()
                        .itemId(item.getItemId())
                        .itemStatusId(item.getItemStatus().getItemStatusId())
                        .itemStatusName(item.getItemStatus().getStatus().getDescription())
                        .name(item.getName())
                        .specification(item.getSpecification())
                        .unit(item.getUnit())
                        .inboundUnitPrice(item.getInboundUnitPrice())
                        .outboundUnitPrice(item.getOutboundUnitPrice())
                        .createdDate(item.getCreatedDate())
                        .updatedDate(item.getUpdatedDate())
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
