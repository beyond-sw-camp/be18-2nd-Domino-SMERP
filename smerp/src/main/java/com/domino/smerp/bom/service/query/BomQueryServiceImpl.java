package com.domino.smerp.bom.service.query;

import com.domino.smerp.bom.dto.response.BomCostCacheResponse;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
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
  public List<BomListResponse> getBomInbound(final Long itemId) {
    final Item item = itemService.findItemById(itemId);
    final BomListResponse root = BomListResponse.fromSelf(item);
    attachChildren(root);
    return List.of(root);
  }

  @Override
  @Transactional(readOnly = true)
  public BomDetailResponse getBomDetail(final Long bomId, final String direction) {
    final Bom bom = bomRepository.findById(bomId)
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
    final Bom bom = bomRepository.findByParentItem_ItemId(parentItemId).stream()
        .findFirst()
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));
    return BomDetailResponse.fromEntity(bom);
  }

  /**
   * BOM 소요량/원가 산출 (Lazy Build 적용)
   */
  @Override
  @Transactional(readOnly = true)
  public BomCostCacheResponse calculateTotalQtyAndCost(final Long rootItemId) {
    // 1. 캐시 조회 (없으면 빌드)
    List<BomCostCache> caches = bomCostCacheRepository.findByRootItemId(rootItemId);
    if (caches.isEmpty()) {
      final Item root = itemService.findItemById(rootItemId);
      caches = bomCacheBuilder.build(root);
      bomCostCacheRepository.saveAll(caches);
    }

    // 2. 캐시 맵핑
    final Map<Long, BomCostCache> cacheMap = caches.stream()
        .collect(Collectors.toMap(BomCostCache::getChildItemId, c -> c));

    // 3. closure 관계 전체 조회 (루트만 X → 전체)
    final List<BomClosure> allEdges = bomClosureRepository.findAll();

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

  // 계층으로 BOM 표현
  private BomCostCacheResponse buildTree(
      final Long itemId,
      final List<BomClosure> allEdges,
      final Map<Long, BomCostCache> cacheMap,
      final int depth
  ) {
    final BomCostCache cache = cacheMap.get(itemId);
    if (cache == null) return null;

    // 현재 노드 기준 직계 자식만 추출
    final List<Long> childrenIds = allEdges.stream()
        .filter(edge -> edge.getAncestorItemId().equals(itemId))
        .filter(edge -> !edge.getDescendantItemId().equals(itemId))
        .filter(edge -> edge.getDepth() == 1) // 직계
        .map(BomClosure::getDescendantItemId)
        .toList();

    final List<BomCostCacheResponse> children = new ArrayList<>();
    BigDecimal totalCost = BigDecimal.ZERO;

    for (final Long childId : childrenIds) {
      final BomCostCacheResponse childNode = buildTree(childId, allEdges, cacheMap, depth + 1);
      if (childNode != null) {
        children.add(childNode);
        totalCost = totalCost.add(childNode.getTotalCost());
      }
    }

    // leaf면 캐시 totalCost 그대로
    if (children.isEmpty()) {
      totalCost = cache.getTotalCost();
    }

    return BomCostCacheResponse.of(cache, depth, totalCost, children);
  }

}
