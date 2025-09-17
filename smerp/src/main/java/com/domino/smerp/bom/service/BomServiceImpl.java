package com.domino.smerp.bom.service;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.dto.response.CreateBomResponse;
import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.bom.repository.BomRepository;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.entity.Item;
import com.domino.smerp.item.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BomServiceImpl implements BomService {

  private final BomRepository bomRepository;

  private final ItemService itemService;


  // BOM 생성
  @Override
  @Transactional
  public CreateBomResponse createBom(final CreateBomRequest request) {
    // ID를 사용하여 각 엔티티를 조회
    Bom parentBom = null;
    if (request.getParentBomId() != null) {
      parentBom = findByBomId(request.getParentBomId());
    }

    Item parentItem = itemService.findItemById(request.getParentItemId());
    Item childItem = itemService.findItemById(request.getChildItemId());

    // 계층 계산 로직
    int depth = 0;
    if (parentBom != null) {
      depth = parentBom.getDepth() + 1;
    }

    Bom bom = Bom.create(request, parentBom, parentItem, childItem, depth);
    Bom savedBom = bomRepository.save(bom);

    return CreateBomResponse.fromEntity(savedBom);
  }

//  @Override
//  @Transactional(readOnly = true)
//  public List<BomListResponse> getAllBoms(){
//
//  };

  // 선택한 품목의 BOM 목록 조회
  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBomByParentItemId(final Long parentItemId) {
    List<Bom> boms = bomRepository.findByParentItemItemId(parentItemId);

    return boms.stream()
        .map(BomListResponse::fromEntity)
        .toList();
  }

  // BOM 상세 조회
  @Override
  @Transactional(readOnly = true)
  public BomDetailResponse getBomDetail(final Long bomId) {
    Bom bom = findByBomId(bomId);
    return BomDetailResponse.fromEntity(bom);
  }

// TODO: 소요량 산출 로직 구현
// ex) 특정 부모품목을 기준으로 하위 BOM들을 재귀 탐색하여 total_qty 계산
// public BigDecimal calculateTotalQty(Long parentItemId) { ... }

// TODO: BOM 수량/비고 수정 메소드
// @Transactional
// public BomDetailResponse updateBom(Long bomId, UpdateBomRequest request) { ... }

// TODO: BOM 삭제/강제삭제 메소드


  // 공통 메소드
  @Override
  @Transactional(readOnly = true)
  public Bom findByBomId(final Long bomId) {
    return bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));
  }

}
