package com.domino.smerp.bom.service.query;

import com.domino.smerp.bom.dto.response.BomCostResponse;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.dto.response.BomRequirementResponse;
import java.math.BigDecimal;
import java.util.List;

public interface BomQueryService {

  List<BomListResponse> getBoms();

  List<BomListResponse> getBomByParentItemId(final Long parentItemId);

  List<BomListResponse> getBomDescendants(final Long itemId);

  BomDetailResponse getBomDetail(final Long bomId, final String direction);

  BomCostResponse calculateTotalQtyAndCost(final Long rootItemId);

  BigDecimal getTotalBomCost(final Long rootItemId);

  // BOM 품목구분 목록 조회
  List<BomListResponse> getBomsByItemStatusId(final Long itemStatusId);

  // BOM 상세 조회
  BomDetailResponse getBomDetailByParentId(final Long parentItemId, final String direction);

}
