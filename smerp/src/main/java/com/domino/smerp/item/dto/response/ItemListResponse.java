package com.domino.smerp.item.dto.response;

import com.domino.smerp.item.Item;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemListResponse {

  private final Long itemId;
  private final String name;
  private final String rfid;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "UTC")
  private final Instant createdAt;

  public static ItemListResponse fromEntity(Item item) {
    return ItemListResponse.builder()
        .itemId(item.getItemId())
        .name(item.getName())
        .rfid(item.getRfid())
        .createdAt(item.getCreatedAt())
        .build();
  }
}