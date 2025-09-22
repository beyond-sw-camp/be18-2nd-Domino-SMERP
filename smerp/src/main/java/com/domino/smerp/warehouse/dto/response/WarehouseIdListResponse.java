package com.domino.smerp.warehouse.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class WarehouseIdListResponse {

  @Builder.Default
  private final List<Long> warehouseIds = new ArrayList<>();
}
