package com.domino.smerp.bom.service.query;

import com.domino.smerp.bom.dto.response.BomAllResponse;
import com.domino.smerp.bom.dto.response.BomCostCacheResponse;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.dto.response.BomRawMaterialListResponse;
import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.bom.entity.BomClosure;
import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.event.BomChangedEvent;
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
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BomQueryServiceImpl implements BomQueryService {

  private final ItemService itemService;

  private final BomRepository bomRepository;
  private final BomCostCacheRepository bomCostCacheRepository;
  private final BomClosureRepository bomClosureRepository;

  private final ApplicationEventPublisher eventPublisher;

  //
  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBoms() {
    return bomRepository.findAll().stream()
        .map(BomListResponse::fromEntity)
        .collect(Collectors.toList());
  }

  // 품목구분 BOM 목록 조회 (캐시X)
  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBomsByItemStatusId(final Long itemStatusId) {
    return bomRepository.findByChildItem_ItemStatus_ItemStatusId(itemStatusId).stream()
        .map(BomListResponse::fromEntity)
        .collect(Collectors.toList());
  }

  // 정전개, 역전개, 원재료리스트 조회 (캐시O)
  @Override
  @Transactional(readOnly = true)
  public BomAllResponse getBomAll(final Long itemId) {
    // 캐시 조회
    List<BomCostCache> caches = bomCostCacheRepository.findByRootItemId(itemId);
    if (caches.isEmpty()) {
      final Item root = itemService.findItemById(itemId);
      eventPublisher.publishEvent(new BomChangedEvent(itemId));
      caches = bomCostCacheRepository.findByRootItemId(itemId);
    }

    // 캐시 맵핑
    final Map<Long, BomCostCache> cacheMap = caches.stream()
        .collect(Collectors.toMap(BomCostCache::getChildItemId, c -> c));

    // closure 관계 조회
    final List<BomClosure> allEdges = bomClosureRepository.findAll();

    // inbound (정전개)
    final BomCostCacheResponse inbound = buildTree(itemId, allEdges, cacheMap, 0);

    // outbound (역전개)
    List<BomCostCacheResponse> outbound =
        List.of(buildOutboundTree(itemId, allEdges, cacheMap, 0));


// rawMaterials (flat list)
    final List<BomRawMaterialListResponse> rawMaterials = caches.stream()
        .filter(
            c -> "원재료".equals(c.getItemStatus().getDescription())) // itemStatusId 대신 description
        .map(BomRawMaterialListResponse::fromCache)
        .toList();

    return BomAllResponse.builder()
        .inbound(inbound)
        .outbound(outbound)
        .rawMaterials(rawMaterials)
        .build();
  }

  // BOM 직계자식만 조회(캐시X)
  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBomByParentItemId(final Long parentItemId) {
    return bomRepository.findByParentItem_ItemId(parentItemId).stream()
        .map(BomListResponse::fromEntity)
        .collect(Collectors.toList());
  }

  // BOM 상세조회 (캐시X)
  @Override
  @Transactional(readOnly = true)
  public BomDetailResponse getBomDetail(final Long bomId, final String direction) {
    final Bom bom = bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));
    return BomDetailResponse.fromEntity(bom);
  }


  // BOM 소요량/원가 산출 (캐시O, Lazy Build 적용)
  @Override
  @Transactional(readOnly = true)
  public BomCostCacheResponse calculateTotalQtyAndCost(final Long rootItemId) {
    // 1. 캐시 조회 (없으면 빌드)
    List<BomCostCache> caches = bomCostCacheRepository.findByRootItemId(rootItemId);
    if (caches.isEmpty()) {
      final Item root = itemService.findItemById(rootItemId);
      eventPublisher.publishEvent(new BomChangedEvent(rootItemId));
      //caches = bomCacheBuilder.build(root);
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


  // ==========================
  // 내부 유틸
  // ==========================

  // 후손들 가져오기
  private void attachChildren(final BomListResponse parentNode) {
    List<Bom> children = bomRepository.findByParentItem_ItemId(parentNode.getItemId());
    for (Bom childBom : children) {
      BomListResponse childNode = BomListResponse.fromEntity(childBom);
      parentNode.getChildren().add(childNode);
      attachChildren(childNode);
    }
  }

  // 정전개 트리 제작
  private BomCostCacheResponse buildTree(
      final Long itemId,
      final List<BomClosure> allEdges,
      final Map<Long, BomCostCache> cacheMap,
      final int depth
  ) {
    final BomCostCache cache = cacheMap.get(itemId);
    if (cache == null) {
      return null;
    }

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

  // 역전개 트리 빌더
  private BomCostCacheResponse buildOutboundTree(
      final Long itemId,
      final List<BomClosure> allEdges,
      final Map<Long, BomCostCache> cacheMap,
      final int depth
  ) {
    final BomCostCache cache = cacheMap.get(itemId);
    if (cache == null) {
      return null;
    }

    // 직계 부모(ancestor) 찾기
    List<Long> parentIds = allEdges.stream()
        .filter(edge -> edge.getDescendantItemId().equals(itemId))
        .filter(edge -> !edge.getAncestorItemId().equals(itemId))
        .filter(edge -> edge.getDepth() == 1)
        .map(BomClosure::getAncestorItemId)
        .toList();

    List<BomCostCacheResponse> parents = new ArrayList<>();
    for (Long parentId : parentIds) {
      BomCostCacheResponse parentNode = buildOutboundTree(parentId, allEdges, cacheMap, depth + 1);
      if (parentNode != null) {
        parents.add(parentNode);
      }
    }

    return BomCostCacheResponse.of(cache, depth, cache.getTotalCost(), parents);
  }


}
