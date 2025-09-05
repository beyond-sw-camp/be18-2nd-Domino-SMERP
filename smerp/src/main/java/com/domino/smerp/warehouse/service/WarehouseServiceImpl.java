package com.domino.smerp.warehouse.service;

import com.domino.smerp.location.service.LocationService;
import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.WarehouseRepository;
import com.domino.smerp.warehouse.dto.WarehouseRequest;
import com.domino.smerp.warehouse.dto.WarehouseResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WarehouseServiceImpl implements WarehouseService {

  private final WarehouseRepository warehouseRepository;

  private final LocationService locationService;

  @Override
  public WarehouseResponse getWarehouseById(Long id) {

    //id 없는 경우
    Warehouse warehouse = warehouseRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("No warehouse of id"));

    return toWarehouseResponse(warehouse);
  }

  @Override
  public List<WarehouseResponse> getAllWarehouses() {

    //empty인 경우 화면에 보임 - 정상 동작이므로 예외 처리 x

    List<WarehouseResponse> warehouseResponses = new ArrayList<>();

    for (Warehouse warehouse : warehouseRepository.findAll()) {
      warehouseResponses.add(toWarehouseResponse(warehouse));
    }

    return warehouseResponses;
  }

  @Override
  @Transactional
  public void deleteWarehouse(Long id) {

    warehouseRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("No warehouse of id"));

    warehouseRepository.deleteById(id);
  }

  @Override
  @Transactional
  public WarehouseResponse createWarehouse(WarehouseRequest warehouseRequest) {

    //name 이미 있는 경우 안됨
    if (warehouseRepository.existsByName(warehouseRequest.getName())) {
      throw new IllegalArgumentException("Warehouse name duplicated");
    }

    Warehouse warehouse = Warehouse.create(warehouseRequest);

    warehouseRepository.save(warehouse);

    //기본 위치 생성해줘야함
    locationService.createLocation(warehouse.getId());

    return toWarehouseResponse(warehouse);
  }

  @Override
  @Transactional
  public WarehouseResponse updateWarehouse(Long id, WarehouseRequest warehouseRequest) {

    Warehouse warehouse = warehouseRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("No warehouse of id"));

    //수정하는 경우 대상 warehouse와 동일 이름 아닌 경우 db에 동일 이름 있을 때 불가
    if (!warehouseRequest.getName().equals(warehouse.getName()) &&
        warehouseRepository.existsByName(warehouseRequest.getName())) {
      throw new IllegalArgumentException("Warehouse name duplicated");
    }

    //warehouse 수정
    warehouse.update(warehouseRequest);

    return toWarehouseResponse(warehouse);
  }

  @Override
  public WarehouseResponse toWarehouseResponse(Warehouse warehouse) {
    WarehouseResponse warehouseResponse = WarehouseResponse.builder()
        .id(warehouse.getId())
        .divisionType(warehouse.getDivisionType())
        .active(warehouse.isActive())
        .address(warehouse.getAddress())
        .zipcode(warehouse.getZipcode())
        .name(warehouse.getName())
        .build();
    return warehouseResponse;
  }

}
