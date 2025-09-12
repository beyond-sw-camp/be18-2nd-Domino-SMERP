package com.domino.smerp.bom.service;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.dto.response.CreateBomResponse;
import com.domino.smerp.bom.entity.Bom;
import java.util.List;

public interface BomService {

  // BOM 생성
  CreateBomResponse createBom(final CreateBomRequest request);
  // BOM 목록 조회
  List<BomListResponse> getBomByParentItemId(final Long parentItemId);
  // BOM 상세 조회
  // BOM 소요량 산출
  // BOM 수량,비고 수정
  // BOM 관계 수정
  // BOM 삭제
  // BOM 강제 삭제



  // BOM 공통 메소드
  Bom findByBomId(final Long bomId);

}
