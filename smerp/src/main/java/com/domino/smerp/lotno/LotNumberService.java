package com.domino.smerp.lotno;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.item.Item;
import com.domino.smerp.lotno.dto.request.CreateLotNumberRequest;
import com.domino.smerp.lotno.dto.request.SearchLotNumberRequest;
import com.domino.smerp.lotno.dto.request.UpdateLotNumberRequest;
import com.domino.smerp.lotno.dto.response.LotNumberDetailResponse;
import com.domino.smerp.lotno.dto.response.LotNumberListResponse;
import com.domino.smerp.lotno.dto.response.LotNumberSimpleResponse;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.domain.Pageable;

public interface LotNumberService {

  // Lot.No 생성
  LotNumberSimpleResponse createLotNumber(final CreateLotNumberRequest request);

  // Lot.No 목록 조회
  PageResponse<LotNumberListResponse> searchLotNumbers(final SearchLotNumberRequest keyword, final Pageable pageable);

  // Lot.No 정전개 조회

  // Lot.No 역전개 조회


  // Lot.No 상세 조회(이력 조회)
  LotNumberDetailResponse getLotNumberById(final Long lotNumberId);

  // Lot.No 수정
  LotNumberDetailResponse updateLotNumber(final Long lotNumberId, final UpdateLotNumberRequest request);

  // Lot.No 삭제
  void deleteLotNumber(final Long lotNumberId);
  void softDeleteByItemId(final Long itemId);

  // 공통 메소드
  // Lot.No 조회
  LotNumber findLotNumberById(final Long lotNumberId);

  String generateLotNumberName(final Instant lotInstant, final Item item);

  LotNumber createLotNumberForStock(final Item item, final BigDecimal qty);

}
