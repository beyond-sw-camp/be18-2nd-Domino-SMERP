package com.domino.smerp.bom.service.command;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.request.UpdateBomRequest;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.CreateBomResponse;
import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.bom.entity.BomClosure;
import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.event.BomChangedEvent;
import com.domino.smerp.bom.repository.BomClosureRepository;
import com.domino.smerp.bom.repository.BomCostCacheRepository;
import com.domino.smerp.bom.repository.BomRepository;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BomCommandServiceImpl implements BomCommandService {

  private final BomRepository bomRepository;
  private final BomClosureRepository bomClosureRepository;
  private final BomCostCacheRepository bomCostCacheRepository;
  private final ItemService itemService;
  private final ApplicationEventPublisher eventPublisher;

  // BOM 생성
  @Override
  @Transactional
  public CreateBomResponse createBom(final CreateBomRequest request) {
    final Item parentItem;
    final Item childItem;

    // 품목 ID가 더 작은 쪽을 먼저 잠금 획득
    if (request.getParentItemId() < request.getChildItemId()) {
      parentItem = itemService.findItemByIdWithLock(request.getParentItemId());
      childItem = itemService.findItemByIdWithLock(request.getChildItemId());
    } else {
      childItem = itemService.findItemByIdWithLock(request.getChildItemId());
      parentItem = itemService.findItemByIdWithLock(request.getParentItemId());
    }

    // 중복 BOM 관계 체크
    if (bomRepository.existsByParentItem_ItemIdAndChildItem_ItemId(
        parentItem.getItemId(), childItem.getItemId())) {
      throw new CustomException(ErrorCode.BOM_DUPLICATE_RELATIONSHIP);
    }

    Bom savedBom = bomRepository.save(Bom.create(request, parentItem, childItem));

    // 클로저 업데이트
    updateBomClosure(parentItem.getItemId(), childItem.getItemId());

    // 이벤트 발행 → Listener에서 캐시 갱신
    eventPublisher.publishEvent(new BomChangedEvent(parentItem.getItemId()));

    return CreateBomResponse.fromEntity(savedBom);
  }

  // BOM 수정
  @Override
  @Transactional
  public BomDetailResponse updateBom(final Long bomId, final UpdateBomRequest request) {
    Bom bom = bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));

    bom.update(request);

    Long oldParentItemId = bom.getParentItem().getItemId();
    Long newParentItemId = request.getParentItemId();

    if (newParentItemId != null && !oldParentItemId.equals(newParentItemId)) {
      Item newParent = itemService.findItemById(newParentItemId);
      bom.updateRelation(request, newParent);

      updateBomClosure(newParentItemId, bom.getChildItem().getItemId());

      // 두 root 모두 invalidate
      eventPublisher.publishEvent(new BomChangedEvent(oldParentItemId));
      eventPublisher.publishEvent(new BomChangedEvent(newParentItemId));
    }

    if (request.getQty() != null) {
      eventPublisher.publishEvent(new BomChangedEvent(bom.getParentItem().getItemId()));
    }

    return BomDetailResponse.fromEntity(bom);
  }

  // BOM 삭제
  @Override
  @Transactional
  public void deleteBom(final Long bomId) {
    Bom bom = bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));

    Long parentId = bom.getParentItem().getItemId();

    bomRepository.delete(bom);
    updateBomClosure(parentId, bom.getChildItem().getItemId());

    eventPublisher.publishEvent(new BomChangedEvent(parentId));
  }

  // BOM 강제 삭제
  @Override
  @Transactional
  public void deleteForceBom(final Long bomId) {
    Bom bom = bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));

    Long targetItemId = bom.getChildItem().getItemId();

    List<BomClosure> descendants = bomClosureRepository.findById_AncestorItemId(targetItemId);
    List<Long> descendantItemIds = descendants.stream()
        .map(BomClosure::getDescendantItemId)
        .collect(Collectors.toList());

    bomRepository.deleteAllByChildItem_ItemIdIn(descendantItemIds);
    bomClosureRepository.deleteByDescendantItemId(targetItemId);

    // 상위 root 들 invalidate
    List<BomClosure> ancestors = bomClosureRepository.findById_DescendantItemId(targetItemId);
    ancestors.stream()
        .map(BomClosure::getAncestorItemId)
        .distinct()
        .forEach(rootId -> eventPublisher.publishEvent(new BomChangedEvent(rootId)));
  }

  // BOM 관계 수정시 클로저 업데이트
  @Override
  @Transactional
  public void updateBomClosure(final Long parentId, final Long childId) {
    // 1. 자기 자신 노드 보장
    bomClosureRepository.upsertBomClosure(childId, childId, 0);

    List<BomClosure> ancestors = bomClosureRepository.findById_DescendantItemId(parentId);
    if (ancestors.isEmpty()) {
      // parent 스스로 루트일 수도 있음
      bomClosureRepository.upsertBomClosure(parentId, parentId, 0);
      ancestors = bomClosureRepository.findById_DescendantItemId(parentId);
    }

    List<BomClosure> descendants = bomClosureRepository.findById_AncestorItemId(childId);
    if (descendants.isEmpty()) {
      // child 스스로 leaf일 수도 있음
      bomClosureRepository.upsertBomClosure(childId, childId, 0);
      descendants = bomClosureRepository.findById_AncestorItemId(childId);
    }

    // 2. 조상 × 자손 조합 모두 upsert
    for (BomClosure ancestor : ancestors) {
      for (BomClosure descendant : descendants) {
        int depth = ancestor.getDepth() + descendant.getDepth() + 1;
        bomClosureRepository.upsertBomClosure(
            ancestor.getAncestorItemId(),
            descendant.getDescendantItemId(),
            depth
        );
      }
    }
  }

  // BOM 캐시 재생성 (Listener에서 호출됨)
  @Override
  @Transactional
  public void rebuildBomCostCache(final Long rootItemId) {
    bomCostCacheRepository.deleteByRootItemId(rootItemId);

    Item rootItem = itemService.findItemById(rootItemId);

    List<BomCostCache> caches = new ArrayList<>();
    dfsBuildCache(rootItem, rootItem, BigDecimal.ONE, 0, caches);

    bomCostCacheRepository.saveAll(caches);
  }

  private void dfsBuildCache(
      final Item root, final Item current, final BigDecimal accQty,
      final int depth, final List<BomCostCache> caches
  ) {
    List<Bom> children = bomRepository.findByParentItem_ItemId(current.getItemId());

    for (Bom childBom : children) {
      Item child = childBom.getChildItem();
      BigDecimal newAccQty = accQty.multiply(childBom.getQty());
      BigDecimal unitCost = child.getInboundUnitPrice();

      caches.add(BomCostCache.create(root.getItemId(), child.getItemId(), depth + 1,
          newAccQty, unitCost));

      dfsBuildCache(root, child, newAccQty, depth + 1, caches);
    }
  }
}
