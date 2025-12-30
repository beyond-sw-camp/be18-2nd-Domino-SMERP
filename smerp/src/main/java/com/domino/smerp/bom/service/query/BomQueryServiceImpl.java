package com.domino.smerp.bom.service.query;

import com.domino.smerp.bom.dto.request.SearchBomRequest;
import com.domino.smerp.bom.dto.response.BomAllResponse;
import com.domino.smerp.bom.dto.response.BomCostCacheResponse;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.dto.response.BomRawMaterialListResponse;
import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.bom.entity.BomClosure;
import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.repository.BomClosureRepository;
import com.domino.smerp.bom.repository.BomCostCacheRepository;
import com.domino.smerp.bom.repository.query.BomQueryRepository;
import com.domino.smerp.bom.support.BomReader;
import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BomQueryServiceImpl implements BomQueryService {

  private final BomReader bomReader;
  private final BomQueryRepository bomQueryRepository;
  private final BomCostCacheRepository bomCostCacheRepository;
  private final BomClosureRepository bomClosureRepository;

  @Override
  @Transactional(readOnly = true)
  public PageResponse<BomListResponse> searchBoms(final SearchBomRequest request,
      final Pageable pageable) {
    return PageResponse.from(
        bomQueryRepository.searchBoms(request, pageable)
    );
  }

  // 정전개, 역전개, 원재료리스트 조회 (캐시O)
  @Override
  @Transactional(readOnly = true)
  public BomAllResponse getBomAll(final Long itemId) {
    // 캐시 조회
    List<BomCostCache> caches = bomCostCacheRepository.findByRootItemId(itemId);

    // 진짜 비어있는지 확인
    if (caches.isEmpty()) {
      throw new CustomException(ErrorCode.BOM_NOT_FOUND);
    }

    // 캐시 맵핑
    final Map<Long, BomCostCache> cacheMap = caches.stream()
        .collect(Collectors.toMap(BomCostCache::getChildItemId, c -> c));

    // closure 관계 조회
    final List<BomClosure> allEdges = bomClosureRepository.findAll();

    // inbound (정전개)
    final BomCostCacheResponse inbound = buildTree(itemId, allEdges, cacheMap, 0);

    // outbound
    final List<Long> allAncestors = allEdges.stream()
        .filter(e -> e.getDescendantItemId().equals(itemId))
        .map(BomClosure::getAncestorItemId)
        .filter(ancestorItemId -> !ancestorItemId.equals(itemId))
        .distinct()
        .toList();

    // self 캐시 맵: key = 아이템ID(=root), value = 그 아이템의 self 캐시 (root=자기자신, child=자기자신, depth=0)
    final Map<Long, BomCostCache> selfCacheMap = new java.util.HashMap<>();

    // 현재 아이템의 self 캐시도 포함
    ensureSelfCache(itemId, selfCacheMap);

    // 모든 조상에 대해 self 캐시 보장
    for (Long aid : allAncestors) {
      ensureSelfCache(aid, selfCacheMap);
    }

    // 본인포함
    final BomCostCacheResponse outboundRoot = buildOutboundTree(itemId, allEdges, selfCacheMap, 0);
    final List<BomCostCacheResponse> outbound =
        outboundRoot != null ? List.of(outboundRoot) : List.of();

    // RawMaterials (원재료 리스트)
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

  // BOM 상세조회 (캐시X)
  @Override
  @Transactional(readOnly = true)
  public BomDetailResponse getBomDetail(final Long bomId, final String direction) {
    final Bom bom = bomReader.findBomById(bomId);
    return BomDetailResponse.fromEntity(bom);
  }


  // BOM 소요량/원가 산출 (캐시O, Lazy Build 제거: 캐시가 없으면 계산하지 않음)
  @Override
  @Transactional(readOnly = true)
  public BomCostCacheResponse calculateTotalQtyAndCost(final Long rootItemId) {
    // 1. 캐시 조회 (없으면 에러 반환)
    List<BomCostCache> caches = bomCostCacheRepository.findByRootItemId(rootItemId);
    if (caches.isEmpty()) {
      throw new CustomException(ErrorCode.BOM_NOT_FOUND);
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
  // 헬퍼 메서드
  // ==========================
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
      final Map<Long, BomCostCache> selfCacheMap,
      final int depth
  ) {
    final BomCostCache self = selfCacheMap.get(itemId);
    if (self == null) {
      return null; // self 캐시조차 없으면 중단
    }

    // 직계 부모들
    final List<Long> parentIds = allEdges.stream()
        .filter(e -> e.getDescendantItemId().equals(itemId))
        .filter(e -> !e.getAncestorItemId().equals(itemId))
        .filter(e -> e.getDepth() == 1)
        .map(BomClosure::getAncestorItemId)
        .toList();

    final List<BomCostCacheResponse> parents = new java.util.ArrayList<>();
    for (Long pid : parentIds) {
      // 부모의 self 캐시 보장은 호출부 ensureSelfCache에서 이미 처리됨
      BomCostCacheResponse parentNode = buildOutboundTree(pid, allEdges, selfCacheMap, depth + 1);
      if (parentNode != null) {
        parents.add(parentNode);
      }
    }

    // self 캐시 값으로 노드 작성 (각 노드는 "자기 루트" 기준의 qty/원가)
    return BomCostCacheResponse.of(self, depth, self.getTotalCost(), parents);
  }

  // self 캐시를 보장: 없으면 빌드하지 않고, 존재하는 경우에만 사용
  private void ensureSelfCache(final Long id, final Map<Long, BomCostCache> selfCacheMap) {
    if (selfCacheMap.containsKey(id)) {
      return;
    }

    // self 캐시 조회 (root=id, child=id)
    bomCostCacheRepository
        .findByRootItemIdAndChildItemId(id, id).ifPresent(self -> selfCacheMap.put(id, self));

  }
}
