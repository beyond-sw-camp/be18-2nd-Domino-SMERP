package com.domino.smerp.bom.service;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.dto.response.CreateBomResponse;
import com.domino.smerp.bom.entity.Bom;
import java.util.List;

public interface BomService {

  // BOM 생성
  CreateBomResponse createBom(final CreateBomRequest request);
  // BOM 목록 조회
  //List<BomListResponse> getAllBoms();

  // 선택한 품목의 BOM 목록 조회
  List<BomListResponse> getBomByParentItemId(final Long parentItemId);

  // BOM 상세 조회
  BomDetailResponse getBomDetail(final Long bomId);

  // TODO: BOM 소요량 산출
  // TODO: BOM 수량, 비고 수정
  // TODO: BOM 관계 수정
  // TODO: BOM 삭제
  // TODO: BOM 강제 삭제

  // 공통 메소드
  Bom findByBomId(final Long bomId);
}
