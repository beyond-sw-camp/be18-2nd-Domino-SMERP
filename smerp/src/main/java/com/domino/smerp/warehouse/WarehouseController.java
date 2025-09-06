package com.domino.smerp.warehouse;

import com.domino.smerp.warehouse.dto.WarehouseRequest;
import com.domino.smerp.warehouse.dto.WarehouseResponse;
import com.domino.smerp.warehouse.service.WarehouseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/warehouses")
@RestController
@RequiredArgsConstructor
public class WarehouseController {

  private final WarehouseService warehouseService;
  //private final LocationService locationService;

  //창고 생성
  @PostMapping
  public ResponseEntity<WarehouseResponse> createWarehouse(
      @RequestBody WarehouseRequest warehouseRequest) {
    WarehouseResponse warehouseResponse = warehouseService.createWarehouse(warehouseRequest);
    return ResponseEntity.status(201).body(warehouseResponse);
  }

  /*
  //창고 위치 중 unfilled 조회
  @GetMapping("/{warehouse-id}/unfilled")
  public ResponseEntity<LocationListResponse> getUnFilledLocations(
      @PathVariable("warehouse-id") Long warehouseId) {
    LocationListResponse locationListResponse = locationService.getUnFilledLocations(warehouseId);

    return ResponseEntity.status(200).body(locationListResponse);
  }
   */

  //창고 목록 조회
  @GetMapping
  public ResponseEntity<List<WarehouseResponse>> getWarehouses() {
    List<WarehouseResponse> warehouseResponses = warehouseService.getAllWarehouses();
    return ResponseEntity.status(200).body(warehouseResponses);
  }

  //창고 상세 조회
  @GetMapping("/{warehouse-id}")
  public ResponseEntity<WarehouseResponse> getWarehouse(
      @PathVariable("warehouse-id") Long warehouseId) {
    WarehouseResponse warehouseResponse = warehouseService.getWarehouseById(warehouseId);
    return ResponseEntity.status(200).body(warehouseResponse);
  }

  //창고 수정
  @PatchMapping("/{warehouse-id}")
  public ResponseEntity<WarehouseResponse> updateWarehouse(
      @PathVariable("warehouse-id") Long warehouseId,
      @RequestBody WarehouseRequest warehouseRequest
  ) {
    WarehouseResponse warehouseResponse = warehouseService.updateWarehouse(warehouseId,
        warehouseRequest);
    return ResponseEntity.status(200).body(warehouseResponse);
  }

  //창고 삭제
  @DeleteMapping("/{warehouse-id}")
  public ResponseEntity deleteWarehouse(@PathVariable("warehouse-id") Long warehouseId) {
    warehouseService.deleteWarehouse(warehouseId);
    return ResponseEntity.status(204).build();
  }
}
