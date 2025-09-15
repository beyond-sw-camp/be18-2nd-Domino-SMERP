package com.domino.smerp.lotno.service;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.entity.Item;
import com.domino.smerp.item.service.ItemService;
import com.domino.smerp.lotno.dto.request.CreateLotNumberRequest;
import com.domino.smerp.lotno.dto.request.SearchLotNumberRequest;
import com.domino.smerp.lotno.dto.request.UpdateLotNumberRequest;
import com.domino.smerp.lotno.dto.response.LotNumberDetailResponse;
import com.domino.smerp.lotno.dto.response.LotNumberListResponse;
import com.domino.smerp.lotno.dto.response.LotNumberSimpleResponse;
import com.domino.smerp.lotno.entity.LotNumber;
import com.domino.smerp.lotno.repository.LotNumberRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
  public LotNumberSimpleResponse createLotNumber(final CreateLotNumberRequest request) {

    final Item item = itemService.findItemById(request.getItemId());

    final String newLotNumberName = generateLotNumberName(request.getLotInstant(), item);

    final LotNumber lotNumber = LotNumber.create(request, item, newLotNumberName);
    final LotNumber savedLotNumber = lotNumberRepository.save(lotNumber);

    return LotNumberSimpleResponse.fromEntity(savedLotNumber);
  }

  // Lot.No 목록 조회
  @Override
  @Transactional(readOnly = true)
  public PageResponse<LotNumberListResponse> searchLotNumbers(final SearchLotNumberRequest keyword,
      final Pageable pageable) {
    return PageResponse
        .from(lotNumberRepository
            .searchLots(keyword, pageable)
            .map(LotNumberListResponse::fromEntity));
  }

  // Lot.No 상세 조회
  @Override
  @Transactional(readOnly = true)
  public LotNumberDetailResponse getLotNumberById(final Long lotNumberId) {
    LotNumber lotNumber = findLotNumberById(lotNumberId);

    if (lotNumber.getItem().isDeleted()) {
      throw new CustomException(ErrorCode.ITEM_NOT_AVAILABLE);
    }

    return LotNumberDetailResponse.fromEntity(lotNumber);
  }

  // Lot.No 수정
  @Override
  @Transactional
  public LotNumberDetailResponse updateLotNumber(final Long lotNumberId,
      final UpdateLotNumberRequest request) {
    LotNumber lotNumber = findLotNumberById(lotNumberId);

    lotNumber.updateLotNumber(request);

    return LotNumberDetailResponse.fromEntity(lotNumber);

  }

  // Lot.No 삭제 (소프트 딜리트)
  @Override
  @Transactional
  public void deleteLotNumber(final Long lotNumberId) {
    LotNumber lotNumber = findLotNumberById(lotNumberId);

    lotNumber.delete();
  }

  // 공통 메소드
  // Lot.No findById
  @Override
  @Transactional(readOnly = true)
  public LotNumber findLotNumberById(final Long lotNumberId) {
    return lotNumberRepository.findById(lotNumberId)
        .orElseThrow(() -> new CustomException(ErrorCode.LOTNUMBER_NOT_FOUND));
  }

  // TODO: count 굳이 필요한지 생각하기, 추후 품목명 -> 품목코드 기준으로 바꿀 것
  // Lot.No 이름을 생성하는 헬퍼 메소드
  private String generateLotNumberName(final Instant lotInstant, final Item item) {

    // 1. 품목명에서 영문과 숫자만 추출 (아직 한글은 구현 못 함)
    final String nameEnglishAndNumbers = item.getName().replaceAll("[^a-zA-Z0-9]", "")
        .toUpperCase();

    // 2. 품목명이 4글자 미만일 경우 0으로 채우고, 4글자를 초과하면 잘라내기
    final String itemPrefix;
    if (nameEnglishAndNumbers.length() < 4) {
      itemPrefix = String.format("%-4s", nameEnglishAndNumbers).replace(' ', '0');
    } else {
      itemPrefix = nameEnglishAndNumbers.substring(0, 4);
    }

    // REVIEW: 굳이 한국 시간으로 변경할 필요가 있을까?
    // 3. Instant를 한국 시간으로 변환해 날짜 포맷
    final String datePrefix = lotInstant
        .atZone(ZoneId.of("Asia/Seoul"))
        .toLocalDate()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    // 4. (날짜-품목명) 결합
    final String lotNumberPrefix = datePrefix + "-" + itemPrefix;

    // 5. DB에서 같은 (날짜-품목명)개수 조회
    final long count = lotNumberRepository.countByNameStartingWith(lotNumberPrefix);

    // 6. count를 기준으로 일련번호 생성
    final String sequenceNumber = String.format("%02d", count + 1);

    // 7. 최종 LotNo 이름 반환
    final String newLotNumberName = lotNumberPrefix + "-" + sequenceNumber;

    // 8. 중복 검사 (동시성 이슈 대비)
    if (lotNumberRepository.existsByName(newLotNumberName)) {
      throw new CustomException(ErrorCode.DUPLICATE_LOTNUMBER);
    }

    return newLotNumberName;
  }

}
