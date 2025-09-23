package com.domino.smerp.bom.service.query;

import com.domino.smerp.bom.dto.request.SearchBomRequest;
import com.domino.smerp.bom.dto.response.BomAllResponse;
import com.domino.smerp.bom.dto.response.BomCostCacheResponse;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.common.dto.PageResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BomQueryService {


  // Bom 목록 페이징
  PageResponse<BomCostCacheResponse> searchBoms(final SearchBomRequest request,final Pageable pageable);

  // BOM 목록 불러오기
  List<BomListResponse> getBoms();

  // BOM 품목구분 목록 조회
  List<BomListResponse> getBomsByItemStatusId(final Long itemStatusId);

  // 정전개, 역전개, 원재료 리스트 한번에
  BomAllResponse getBomAll(final Long itemId);

  // 선택한 품목의 직계 자손만 불러오기
  List<BomListResponse> getBomByParentItemId(final Long parentItemId);

  // BOM 상세 조회(정전개, 역전개)
  BomDetailResponse getBomDetail(final Long bomId, final String direction);

  // BOM 소요량 계산 및 산출
  BomCostCacheResponse calculateTotalQtyAndCost(final Long rootItemId);

  Bom findBomById(final Long bomId);

}
