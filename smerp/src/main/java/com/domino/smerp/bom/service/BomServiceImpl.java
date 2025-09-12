package com.domino.smerp.bom.service;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
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

  // BOM 목록 조회
  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBomByParentItemId(Long parentItemId) {
    List<Bom> boms = bomRepository.findByParentItemItemId(parentItemId);

    return boms.stream()
        .map(BomListResponse::fromEntity)
        .toList();
  }


  // 공통 메소드
  @Override
  @Transactional(readOnly = true)
  public Bom findByBomId(final Long bomId) {
    return bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));
  }

}
