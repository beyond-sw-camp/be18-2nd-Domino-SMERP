package com.domino.smerp.warehouse.dto.response;

import com.domino.smerp.warehouse.constants.DivisionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class WarehouseResponse {

  private final Long id;

  private final String name;

  private final DivisionType divisionType;

  private final boolean active;

  private final String address;

  private final String zipcode;
}
