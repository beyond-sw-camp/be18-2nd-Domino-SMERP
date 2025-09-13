package com.domino.smerp.lotno.service;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.service.ItemService;
import com.domino.smerp.item.service.ItemServiceImpl;
import com.domino.smerp.lotno.dto.request.CreateLotNumberRequest;
import com.domino.smerp.lotno.dto.request.LotNumberSearchRequest;
import com.domino.smerp.lotno.dto.request.UpdateLotNumberRequest;
import com.domino.smerp.lotno.dto.response.LotNumberDetailResponse;
import com.domino.smerp.lotno.dto.response.LotNumberListResponse;
import com.domino.smerp.lotno.entity.LotNumber;
import com.domino.smerp.lotno.repository.LotNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LotNumberServiceImpl implements LotNumberService {

  private final LotNumberRepository lotNumberRepository;
  private final ItemService itemService;

  // Lot.No 수동 생성
  @Override
  @Transactional
  public LotNumberDetailResponse createLots(final CreateLotNumberRequest request) {
    return null;
  }

  // Lot.No 이력 조회
  @Override
  @Transactional(readOnly = true)
  public PageResponse<LotNumberListResponse> searchLotNumbers(final LotNumberSearchRequest keyword,
      final Pageable pageable) {
    return null;
  }

  // Lot.No 상세 조회
  @Override
  @Transactional(readOnly = true)
  public LotNumberDetailResponse getLotNumberById(final Long lotNumberId) {
    return null;
  }

  // Lot.No 수정()
  @Override
  @Transactional
  public LotNumberDetailResponse updateLotNumber(final Long lotNumberId,
      final UpdateLotNumberRequest request) {
    return null;
  }

  // Lot.No 삭제 (소프트 딜리트)
  @Override
  @Transactional
  public void deleteLotNumber(final Long lotNumberId) {
    final LotNumber lotNumber = findLotNumberById(lotNumberId);

    lotNumber.delete();
  }

  // 공통 부분

  // Lot.No findById
  public LotNumber findLotNumberById(final Long lotNumberId) {
    return lotNumberRepository.findById(lotNumberId)
        .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
  }


}
