package com.domino.smerp.warehouse.service;

import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.dto.WarehouseRequest;
import com.domino.smerp.warehouse.dto.WarehouseResponse;
import java.util.List;

public interface WarehouseService {

  WarehouseResponse getWarehouseById(Long id);

  List<WarehouseResponse> getAllWarehouses();

  void deleteWarehouse(Long id);

  WarehouseResponse updateWarehouse(Long id, WarehouseRequest warehouseRequest);

  WarehouseResponse createWarehouse(WarehouseRequest warehouseRequest);

  WarehouseResponse toWarehouseResponse(Warehouse warehouse);
}
