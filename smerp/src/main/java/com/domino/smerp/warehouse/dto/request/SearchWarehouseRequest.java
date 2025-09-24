package com.domino.smerp.warehouse.dto.request;

import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.constants.DivisionType;
import com.domino.smerp.warehouse.dto.response.WarehouseResponse;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchWarehouseRequest {
  private final String warehouseName;
  private final boolean active;
  private final DivisionType divisionType;
  private final String address;
  private final String zipcode;

}
