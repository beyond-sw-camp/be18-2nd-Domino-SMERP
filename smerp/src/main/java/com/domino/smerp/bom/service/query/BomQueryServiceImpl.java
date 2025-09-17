package com.domino.smerp.bom.service.query;

import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.dto.response.BomRequirementResponse;
import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.repository.BomClosureRepository;
import com.domino.smerp.bom.repository.BomCostCacheRepository;
import com.domino.smerp.bom.repository.BomRepository;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BomQueryServiceImpl implements BomQueryService {

  private final BomClosureRepository bomClosureRepository;
  private final BomRepository bomRepository;
  private final BomCostCacheRepository bomCostCacheRepository;
  private final ItemService itemService;


  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBoms() {
    return bomRepository.findAll().stream()
        .map(BomListResponse::fromEntity)
        .collect(Collectors.toList());
  }


  // BOM 목록 조회 (직계 자식만)
  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBomByParentItemId(final Long parentItemId) {
    List<Bom> boms = bomRepository.findByParentItem_ItemId(parentItemId);
    return boms.stream()
        .map(BomListResponse::fromEntity)
        .collect(Collectors.toList());
  }

  // 특정 품목의 BOM 계층 구조 조회
  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBomDescendants(final Long itemId) {
    Item item = itemService.findItemById(itemId);
    BomListResponse root = BomListResponse.fromSelf(item);
    attachChildren(root);
    return List.of(root);
  }




  // BOM 상세 조회
  @Override
  @Transactional(readOnly = true)
  public BomDetailResponse getBomDetail(final Long bomId, final String direction) {
    Bom bom = bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));
    return BomDetailResponse.fromEntity(bom);
  }

  // TODO: BOM 소요량 산출 로직
  // 캐시 테이블을 활용하여 BOM 정전개 및 소요량 계산
  @Override
  @Transactional(readOnly = true)
  public List<BomRequirementResponse> calculateTotalQtyAndCost(Long rootItemId) {
    List<BomCostCache> caches = bomCostCacheRepository.findByRootItemId(rootItemId);

    return caches.stream()
        .map(cache -> {
          String childItemName = itemService.findItemById(cache.getChildItemId()).getName();
          return BomRequirementResponse.fromEntity(cache, childItemName);
        })
        .collect(Collectors.toList());
  }


  // 후손들 찾아오는 헬퍼메소드
  private void attachChildren(BomListResponse parentNode) {
    List<Bom> children = bomRepository.findByParentItem_ItemId(parentNode.getItemId());

    for (Bom childBom : children) {
      BomListResponse childNode = BomListResponse.fromEntity(childBom);
      parentNode.getChildren().add(childNode);

      // 재귀적으로 하위 자식도 탐색
      attachChildren(childNode);
    }

  }
}