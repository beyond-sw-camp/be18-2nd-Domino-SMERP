package com.domino.smerp.bom.service.query;

import com.domino.smerp.bom.dto.response.BomCostResponse;
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

  // BOM 품목구분 목록 조회
  @Override
  @Transactional(readOnly = true)
  public List<BomListResponse> getBomsByItemStatusId(final Long itemStatusId) {
    return bomRepository.findByChildItem_ItemStatus_ItemStatusId(itemStatusId).stream()
        .map(BomListResponse::fromEntity)
        .collect(Collectors.toList());
  }

  // BOM 상세 조회 (부모 품목 ID 기반)
  @Override
  @Transactional(readOnly = true)
  public BomDetailResponse getBomDetailByParentId(final Long parentItemId, final String direction) {
    // 부모 품목 ID로 BOM을 찾고, 여러 개일 수 있으므로 적절한 로직이 필요합니다.
    // 여기서는 간단히 첫 번째 BOM 관계를 반환하도록 가정합니다.
    Bom bom = bomRepository.findByParentItem_ItemId(parentItemId).stream()
        .findFirst()
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));
    return BomDetailResponse.fromEntity(bom);
  }

  /**
   * BOM 소요량/원가 산출 (Lazy Build 적용) - 캐시 없으면 즉시 DFS로 빌드 후 저장
   */
  @Override
  @Transactional(readOnly = true)
  public List<BomRequirementResponse> calculateTotalQtyAndCost(final Long rootItemId) {
    List<BomCostCache> caches = bomCostCacheRepository.findByRootItemId(rootItemId);

    if (caches.isEmpty()) {
      // Lazy rebuild
      Item root = itemService.findItemById(rootItemId);
      caches = rebuildBomCostCache(root);
      bomCostCacheRepository.saveAll(caches);
    }

    return caches.stream()
        .map(cache -> {
          String childItemName = itemService.findItemById(cache.getChildItemId()).getName();
          return BomRequirementResponse.fromEntity(cache, childItemName);
        })
        .collect(Collectors.toList());
  }


  /**
   * Lazy Build용 캐시 생성 로직
   */
  private List<BomCostCache> rebuildBomCostCache(final Item root) {
    List<BomCostCache> caches = new ArrayList<>();
    dfsBuildCache(root, root, BigDecimal.ONE, 0, caches);
    return caches;
  }

  /**
   * DFS로 BOM 트리를 순회하며 누적 qty와 원가를 계산 (리프 기반)
   */
  private BigDecimal dfsBuildCache(
      final Item root,
      final Item current,
      final BigDecimal accQty,
      final int depth,
      final List<BomCostCache> caches
  ) {
    final List<Bom> children = bomRepository.findByParentItem_ItemId(current.getItemId());

    // 리프 노드라면 → 원가 직접 계산
    if (children.isEmpty()) {
      final BigDecimal unitCost = current.getInboundUnitPrice();
      final BigDecimal totalCost = accQty.multiply(unitCost);

      caches.add(BomCostCache.create(
          root.getItemId(),
          current.getItemId(),
          depth,
          accQty,
          totalCost
      ));
      return totalCost;
    }

    // 내부 노드라면 → 자식들의 총 원가 합산
    BigDecimal totalCost = BigDecimal.ZERO;
    for (final Bom childBom : children) {
      final Item child = childBom.getChildItem();
      final BigDecimal newAccQty = accQty.multiply(childBom.getQty());

      BigDecimal childCost = dfsBuildCache(root, child, newAccQty, depth + 1, caches);
      totalCost = totalCost.add(childCost);
    }

    // 내부 노드도 캐시에 기록 (리프 원가의 합을 단가 대신 저장)
    caches.add(BomCostCache.create(
        root.getItemId(),
        current.getItemId(),
        depth,
        accQty,
        totalCost
    ));

    return totalCost;
  }

  // 후손들 찾아오는 헬퍼메소드
  private void attachChildren(final BomListResponse parentNode) {
    List<Bom> children = bomRepository.findByParentItem_ItemId(parentNode.getItemId());
    for (Bom childBom : children) {
      BomListResponse childNode = BomListResponse.fromEntity(childBom);
      parentNode.getChildren().add(childNode);
      attachChildren(childNode);
    }
  }

  // 캐시 기반 트리 빌드
  private List<BomCostResponse> buildTreeFromCache(final List<BomCostCache> caches) {
    Map<Long, BomCostResponse> map = new HashMap<>();
    List<BomCostResponse> roots = new ArrayList<>();

    for (BomCostCache cache : caches) {
      BomCostResponse node = BomCostResponse.fromCache(cache);
      map.put(cache.getChildItemId(), node);

      if (cache.getDepth() == 1) {
        roots.add(node); // root 직계 자식
      } else {
        BomCostResponse parent = map.get(cache.getRootItemId());
        if (parent != null) {
          parent.getChildren().add(node);
        }
      }
    }
    return roots;
  }
}
