package com.domino.smerp.warehouse.service;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.lotno.dto.response.LotNumberListResponse;
import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.dto.request.SearchWarehouseRequest;
import com.domino.smerp.warehouse.dto.request.WarehouseRequest;
import com.domino.smerp.warehouse.dto.response.WarehouseIdListResponse;
import com.domino.smerp.warehouse.dto.response.WarehouseListResponse;
import com.domino.smerp.warehouse.dto.response.WarehouseResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface WarehouseService {

  WarehouseResponse getWarehouseById(final Long id);

  List<WarehouseResponse> getAllWarehouses();

  PageResponse<WarehouseListResponse> searchWarehouses(
      final SearchWarehouseRequest keyword,
      final Pageable pageable);

  void deleteWarehouse(final Long id);

  WarehouseResponse updateWarehouse(final Long id, final WarehouseRequest warehouseRequest);

  WarehouseResponse createWarehouse(final WarehouseRequest warehouseRequest);

  WarehouseIdListResponse getAllUnFilledWarehouses();

  WarehouseResponse toWarehouseResponse(final Warehouse warehouse);
}
