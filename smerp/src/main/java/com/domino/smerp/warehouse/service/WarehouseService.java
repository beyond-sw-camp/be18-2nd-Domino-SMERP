package com.domino.smerp.warehouse.service;

import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.dto.WarehouseRequest;
import com.domino.smerp.warehouse.dto.response.WarehouseIdListResponse;
import com.domino.smerp.warehouse.dto.response.WarehouseResponse;
import java.util.List;

public interface WarehouseService {

  WarehouseResponse getWarehouseById(final Long id);

  List<WarehouseResponse> getAllWarehouses();

  void deleteWarehouse(final Long id);

  WarehouseResponse updateWarehouse(final Long id, final WarehouseRequest warehouseRequest);

  WarehouseResponse createWarehouse(final WarehouseRequest warehouseRequest);

  WarehouseIdListResponse getAllUnFilledWarehouses();

  WarehouseResponse toWarehouseResponse(final Warehouse warehouse);
}
