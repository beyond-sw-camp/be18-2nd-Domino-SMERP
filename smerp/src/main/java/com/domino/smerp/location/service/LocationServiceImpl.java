package com.domino.smerp.location.service;

import com.domino.smerp.location.Location;
import com.domino.smerp.location.LocationRepository;
import com.domino.smerp.location.dto.request.LocationIdListRequest;
import com.domino.smerp.location.dto.response.LocationIdListResponse;
import com.domino.smerp.location.dto.response.LocationListResponse;
import com.domino.smerp.location.dto.response.LocationResponse;
import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

  private final WarehouseRepository warehouseRepository;
  private final LocationRepository locationRepository;

  //칸 생성(창고 생성 시)
  @Override
  @Transactional
  public void createLocation(final Long warehouseId) {

    Warehouse warehouse = warehouseRepository.findById(warehouseId)
        .orElseThrow(() -> new EntityNotFoundException("창고를 찾을 수 없습니다."));

    List<String> racks = parseFixedRange("R", 1, 20);
    List<String> levels = parseFixedRange("L", 1, 5);
    List<String> bins = parseFixedRange("B", 1, 30);

    //warehouse는 id 기준
    String w = parseId("W", warehouse.getId(), 2);

    List<Location> locations = new ArrayList<>();

    //rfid 생성, location 저장
    for (String r : racks) {
      for (String l : levels) {
        for (String b : bins) {
          String rfid = generateRfid(w, r, l, b);
          Location location = Location.create(r, l, b, rfid, warehouse);
          locations.add(location);
          warehouse.getLocations().add(location); //
        }
      }
    }

    List<Location> savedLocations = locationRepository.saveAll(locations);
    // 확인용 출력
    /*
    savedLocations.forEach(loc -> System.out.println(
        loc.getId() + " " + loc.getRackNo() + " " + loc.getLevelNo() + " " + loc.getBinNo() + " " + loc.getBinRfid()
    ));
     */


  }

  private List<String> parseFixedRange(final String prefix, final int start, final int end) {
    List<String> result = new ArrayList<>();
    for (int i = start; i <= end; i++) {
      if (i < 10) { //10 밑 R01..
        result.add(prefix + String.format("%02d", i));
      } else { //10부터는 R10..
        result.add(prefix + i);
      }
    }
    return result;
  }

  private String parseId(final String prefix, final Long id, final Integer padding) {

    String result = "";
    if (id < 10) { //10 밑 W01..
      result = prefix + String.format("%02d", id);
    } else { //10부터 W10..
      result = prefix + id;
    }
    return result;
  }

  private String generateRfid(final String w, final String r, final String l, final String b) {
    String rfid = "RFID" + "-" + w + "-" + r + "-" + l + "-" + b;
    return rfid;
  }

  //빈 칸 반환
  @Transactional(readOnly = true)
  @Override
  public LocationListResponse getUnFilledLocations(final Long warehouseId) {

    List<Location> unFilledLocations = locationRepository.findAllByWarehouseIdAndFilledFalse(
        warehouseId);

    List<LocationResponse> responses = new ArrayList<>();

    for (Location location : unFilledLocations) {

      responses.add(LocationResponse.builder()
          .id(location.getId())
          .rackNo(location.getRackNo())
          .levelNo(location.getLevelNo())
          .binNo(location.getBinNo())
          .filled(location.isFilled())
          .build());
    }

    return LocationListResponse.builder()
        .locationResponses(responses)
        .build();

  }

  //칸 채움
  @Transactional
  @Override
  public LocationIdListResponse fillLocations(final LocationIdListRequest locationIdListRequest) {

    LocationIdListResponse locationIdListResponse = LocationIdListResponse.builder().build();

    for (Long id : locationIdListRequest.getLocationIds()) {

      //유효 x id
      Location location = locationRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException("id에 해당하는 location 없음"));

      location.setFilled(true);
      locationIdListResponse.getLocationIds().add(location.getId());

    }

    return locationIdListResponse;

  }

  //칸 비움
  @Transactional
  @Override
  public LocationIdListResponse unFillLocations(final LocationIdListRequest locationIdListRequest) {

    LocationIdListResponse locationIdListResponse = LocationIdListResponse.builder().build();

    for (Long id : locationIdListRequest.getLocationIds()) {
      Location location = locationRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException("id에 해당하는 location 없음"));

      location.setFilled(false);
      locationIdListResponse.getLocationIds().add(location.getId());
    }

    return locationIdListResponse;

  }


  private LocationResponse toLocationResponse(final Location location) {
    return LocationResponse.builder()
        .id(location.getId())
        .rackNo(location.getRackNo())
        .levelNo(location.getLevelNo())
        .binNo(location.getBinNo())
        .filled(location.isFilled())
        .build();

  }
}
