package com.domino.smerp.lotno.controller;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.lotno.dto.request.CreateLotNumberRequest;
import com.domino.smerp.lotno.dto.request.LotNumberSearchRequest;
import com.domino.smerp.lotno.dto.request.UpdateLotNumberRequest;
import com.domino.smerp.lotno.dto.response.LotNumberDetailResponse;
import com.domino.smerp.lotno.dto.response.LotNumberListResponse;
import com.domino.smerp.lotno.service.LotNumberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lots")
@RequiredArgsConstructor
public class LotNumberController {

  private final LotNumberService lotNumberService;

  // Lot.No 생성
  @PostMapping
  public ResponseEntity<LotNumberDetailResponse> createLots(final @Valid @RequestBody
      CreateLotNumberRequest request){

    return ResponseEntity.ok(lotNumberService.createLots(request));
  }

  // Lot.No 목록 조회 (검색조건은 쿼리파라미터로)
  @GetMapping
  public ResponseEntity<PageResponse<LotNumberListResponse>> getLotNumbers(
      @ModelAttribute LotNumberSearchRequest keyword,
      Pageable pageable) {
    return ResponseEntity.ok(lotNumberService.searchLotNumbers(keyword, pageable));
  }

  // Lot.No (이력)상세 조회
  @GetMapping("/{lots-id}")
  public ResponseEntity<LotNumberDetailResponse> getLotNumberById(
      @PathVariable("lots-id") final Long lotNumberId) {
    return ResponseEntity.ok(lotNumberService.getLotNumberById(lotNumberId));
  }

  // Lot.No 수정
  @PatchMapping("/{lots-id}")
  public ResponseEntity<LotNumberDetailResponse> updateLotNumber(@PathVariable("lots-id") final Long lotNumberId,
      final @Valid @RequestBody UpdateLotNumberRequest request) {
    return ResponseEntity.ok(lotNumberService.updateLotNumber(lotNumberId, request));
  }


  // Lot.No 삭제
  @DeleteMapping("/{lots-id}")
  public ResponseEntity<Void> deleteLotNumber(@PathVariable("lots-id") final Long lotNumberId) {
    lotNumberService.deleteLotNumber(lotNumberId);
    return ResponseEntity.noContent().build();
  }


}
