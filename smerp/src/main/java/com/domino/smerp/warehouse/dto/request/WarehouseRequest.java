package com.domino.smerp.warehouse.dto.request;

import com.domino.smerp.warehouse.constants.DivisionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WarehouseRequest {

  private final String name;

  private final DivisionType divisionType;

  private final Boolean active; //수정시 null 가능, getActive

  private final String address;

  private final String zipcode;
}
