package com.domino.smerp.item.dto.response;

import com.domino.smerp.item.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemListResponse {

  private final Long itemId;
  private final String name;
  private final String unit;
  private final String itemStatusName;
  private final String rfid;
  private final String itemAct;
  private final String groupName1;
  private final String groupName2;
  private final String groupName3;

  public static ItemListResponse fromEntity(Item item) {
    return ItemListResponse.builder()
        .itemId(item.getItemId())
        .itemStatusName(item.getItemStatus().getStatus().getDescription())
        .name(item.getName())
        .unit(item.getUnit())
        .rfid(item.getRfid())
        .itemAct(item.getItemAct().getDescription())
        .groupName1(item.getGroupName1())
        .groupName2(item.getGroupName2())
        .groupName3(item.getGroupName3())
        .build();
  }
}