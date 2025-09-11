package com.domino.smerp.lotno.service;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.lotno.dto.request.CreateLotNumberRequest;
import com.domino.smerp.lotno.dto.request.LotNumberSearchRequest;
import com.domino.smerp.lotno.dto.request.UpdateLotNumberRequest;
import com.domino.smerp.lotno.dto.response.LotNumberDetailResponse;
import com.domino.smerp.lotno.dto.response.LotNumberListResponse;
import org.springframework.data.domain.Pageable;

public interface LotNumberService {

  // Lot.No 생성
  LotNumberDetailResponse createLots(final CreateLotNumberRequest request);

  // Lot.No 목록 조회
  PageResponse<LotNumberListResponse> searchLotNumbers(final LotNumberSearchRequest keyword, final Pageable pageable);

  // Lot.No 조회
  LotNumberDetailResponse getLotNumberById(final Long lotNumberId);

  // Lot.No 수정
  LotNumberDetailResponse updateLotNumber(final Long lotNumberId, final UpdateLotNumberRequest request);

  // Lot.No 삭제
  void deleteLotNumber(final Long lotNumberId);



}
