package com.domino.smerp.bom.service.command;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.request.UpdateBomRequest;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.CreateBomResponse;

public interface BomCommandService {

  // BOM 생성
  CreateBomResponse createBom(final CreateBomRequest request);

  // BOM 수정(관계, 수량, 비고)
  BomDetailResponse updateBom(final Long bomId, final UpdateBomRequest request);

  // BOM 삭제 (부모 있으면 불가능)
  void deleteBom(final Long bomId);

  // BOM 자식 있어도 강제 삭제
  void deleteForceBom(final Long bomId);

  // BOM 관계 수정시 계층 재계산
  void updateBomClosure(final Long parentId, final Long childId);

  // BOM 캐시 재생성
  void rebuildBomCostCache(final Long rootItemId);

}
