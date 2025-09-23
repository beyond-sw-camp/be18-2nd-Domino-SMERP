package com.domino.smerp.warehouse.dto.response;

import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.constants.DivisionType;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WarehouseListResponse {
  private final Long id;
  private final String name;
  private final DivisionType divisionType;
  private final boolean active;
  private final String address;
  private final String zipcode;

  public static WarehouseListResponse fromEntity(Warehouse warehouse) {
    return WarehouseListResponse.builder()
        .id(warehouse.getId())
        .name(warehouse.getName())
        .divisionType(warehouse.getDivisionType())
        .active(warehouse.isActive())
        .address(warehouse.getAddress())
        .zipcode(warehouse.getZipcode())
        .build();
  }

}


