package com.domino.smerp.bom.service.query;

import com.domino.smerp.bom.dto.response.BomCostResponse;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.dto.response.BomRequirementResponse;
import java.math.BigDecimal;
import java.util.List;

public interface BomQueryService {

  // BOM 목록 불러오기
  List<BomListResponse> getBoms();

  // 선택한 품목의 직계 자손만 불러오기
  List<BomListResponse> getBomByParentItemId(final Long parentItemId);

  // 선택한 품목의 모든 후손들 불러오기 (정전개)
  List<BomListResponse> getBomInbound(final Long itemId);

  // BOM 상세 조회(정전개, 역전개)
  BomDetailResponse getBomDetail(final Long bomId, final String direction);

  // BOM 소요량 계산 및 산출
  BomCostResponse calculateTotalQtyAndCost(final Long rootItemId);

  BigDecimal getTotalBomCost(final Long rootItemId);

  // BOM 품목구분 목록 조회
  List<BomListResponse> getBomsByItemStatusId(final Long itemStatusId);

  // BOM 상세 조회
  BomDetailResponse getBomDetailByParentId(final Long parentItemId, final String direction);

}
