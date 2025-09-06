package com.domino.smerp.warehouse.dto;

import com.domino.smerp.warehouse.constants.DivisionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WarehouseRequest {

  private long id;

  private String name;

  private DivisionType divisionType;

  private Boolean active; //수정시 null 가능, getActive

  private String address;

  private String zipcode;
}
