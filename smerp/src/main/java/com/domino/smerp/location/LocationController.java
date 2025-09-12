package com.domino.smerp.location;

import com.domino.smerp.location.dto.request.LocationIdListRequest;
import com.domino.smerp.location.dto.response.LocationIdListResponse;
import com.domino.smerp.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/locations")
@RestController
@RequiredArgsConstructor
//생성 삭제 상세 조회 x (창고 따라)
//목록 조회, 생성 조회 창고 따라
public class LocationController {

  private final LocationService locationService;

  //fill
  @PatchMapping("/fill")
  public ResponseEntity<LocationIdListResponse> fillLocations(
      @RequestBody LocationIdListRequest locationIdListRequest) {
    LocationIdListResponse locationIdListResponse = locationService.fillLocations(
        locationIdListRequest);
    return ResponseEntity.status(200).body(locationIdListResponse);
  }

  //unfill
  @PatchMapping("/unfill")
  public ResponseEntity<LocationIdListResponse> unFillLocations(
      @RequestBody LocationIdListRequest locationIdListRequest) {
    LocationIdListResponse locationIdListResponse = locationService.unFillLocations(
        locationIdListRequest);
    return ResponseEntity.status(200).body(locationIdListResponse);
  }

}
