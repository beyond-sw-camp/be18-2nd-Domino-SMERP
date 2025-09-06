package com.domino.smerp.warehouse.dto;

import com.domino.smerp.warehouse.constants.DivisionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor

public class WarehouseResponse {

  private long id;

  private String name;

  private DivisionType divisionType;

  private boolean active;

  private String address;

  private String zipcode;
}
