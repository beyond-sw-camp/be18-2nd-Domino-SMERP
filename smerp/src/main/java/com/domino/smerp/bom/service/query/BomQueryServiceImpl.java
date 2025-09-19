package com.domino.smerp.bom.service.query;

import com.domino.smerp.bom.dto.response.BomCostResponse;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.dto.response.BomRequirementResponse;
import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.bom.entity.BomClosure;
import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.repository.BomClosureRepository;
import com.domino.smerp.bom.repository.BomRepository;
import com.domino.smerp.bom.repository.BomCostCacheRepository;
import com.domino.smerp.bom.service.cache.BomCacheBuilder;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BomQueryServiceImpl implements BomQueryService {

  private final BomRepository bomRepository;
  private final BomCostCacheRepository bomCostCacheRepository;
  private final BomClosureRepository bomClosureRepository;
  private final BomCacheBuilder bomCacheBuilder;
  private final ItemService itemService;

  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBoms() {
    return bomRepository.findAll().stream()
        .map(BomListResponse::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBomByParentItemId(final Long parentItemId) {
    return bomRepository.findByParentItem_ItemId(parentItemId).stream()
        .map(BomListResponse::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBomDescendants(final Long itemId) {
    Item item = itemService.findItemById(itemId);
    BomListResponse root = BomListResponse.fromSelf(item);
    attachChildren(root);
    return List.of(root);
  }

  @Override
  @Transactional(readOnly = true)
  public BomDetailResponse getBomDetail(final Long bomId, final String direction) {
    Bom bom = bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));
    return BomDetailResponse.fromEntity(bom);
  }

  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBomsByItemStatusId(final Long itemStatusId) {
    return bomRepository.findByChildItem_ItemStatus_ItemStatusId(itemStatusId).stream()
        .map(BomListResponse::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public BomDetailResponse getBomDetailByParentId(final Long parentItemId, final String direction) {
    Bom bom = bomRepository.findByParentItem_ItemId(parentItemId).stream()
        .findFirst()
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));
    return BomDetailResponse.fromEntity(bom);
  }

  /**
   * BOM 소요량/원가 산출 (Lazy Build 적용)
   */
  @Override
  @Transactional(readOnly = true)
  public BomCostResponse calculateTotalQtyAndCost(final Long rootItemId) {
    // 1. 캐시 조회 (없으면 빌드)
    List<BomCostCache> caches = bomCostCacheRepository.findByRootItemId(rootItemId);
    if (caches.isEmpty()) {
      Item root = itemService.findItemById(rootItemId);
      caches = bomCacheBuilder.build(root);
      bomCostCacheRepository.saveAll(caches);
    }

    // 2. 캐시 맵핑
    Map<Long, BomCostCache> cacheMap = caches.stream()
        .collect(Collectors.toMap(BomCostCache::getChildItemId, c -> c));

    // 3. closure 관계 조회
    List<BomClosure> allEdges = bomClosureRepository.findById_AncestorItemId(rootItemId);

    // 4. 트리 빌드
    return buildTree(rootItemId, allEdges, cacheMap, 0);
  }

  @Override
  @Transactional(readOnly = true)
  public BigDecimal getTotalBomCost(final Long rootItemId) {
    return bomCostCacheRepository.getTotalCost(rootItemId);
  }

  // ==========================
  // 내부 유틸
  // ==========================

  private void attachChildren(final BomListResponse parentNode) {
    List<Bom> children = bomRepository.findByParentItem_ItemId(parentNode.getItemId());
    for (Bom childBom : children) {
      BomListResponse childNode = BomListResponse.fromEntity(childBom);
      parentNode.getChildren().add(childNode);
      attachChildren(childNode);
    }
  }

  private BomCostResponse buildTree(Long itemId,
      List<BomClosure> allEdges,
      Map<Long, BomCostCache> cacheMap,
      int depth) {
    BomCostCache cache = cacheMap.get(itemId);
    if (cache == null) {
      return null;
    }

    // 자식 노드 찾기
    List<Long> childrenIds = allEdges.stream()
        .filter(edge -> edge.getAncestorItemId().equals(itemId) && edge.getDepth() == 1)
        .map(BomClosure::getDescendantItemId)
        .toList();

    // 자식 노드 재귀 호출
    List<BomCostResponse> children = new ArrayList<>();
    BigDecimal totalCost = BigDecimal.ZERO;

    for (Long childId : childrenIds) {
      BomCostResponse childNode = buildTree(childId, allEdges, cacheMap, depth + 1);
      if (childNode != null) {
        children.add(childNode);
        totalCost = totalCost.add(childNode.getTotalCost());
      }
    }

    // 내부 노드 → 자식 합계, 리프 노드 → 캐시 totalCost 그대로 사용
    if (children.isEmpty()) {
      totalCost = cache.getTotalCost();
    }

    return BomCostResponse.of(cache, depth, totalCost, children);
  }

}
