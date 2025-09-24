package com.domino.smerp.warehouse.repository;

import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.dto.request.SearchWarehouseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseQueryRepository {
  Page<Warehouse> searchWarehouses(final SearchWarehouseRequest keyword, final Pageable pageable);

}
