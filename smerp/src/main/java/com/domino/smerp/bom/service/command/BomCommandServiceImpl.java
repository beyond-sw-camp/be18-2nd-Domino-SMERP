package com.domino.smerp.bom.service.command;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.request.UpdateBomRelationRequest;
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
import com.domino.smerp.bom.service.cache.BomCacheBuilder;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
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
  private final BomCacheBuilder bomCacheBuilder;
  private final ItemService itemService;

  private final ApplicationEventPublisher eventPublisher;

  private static final ConcurrentHashMap<Long, ReentrantLock> closureLocks = new ConcurrentHashMap<>();


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
    Bom bom = findBomById(bomId);

    // 수량과 비고만 업데이트
    bom.update(request);

    if (request.getQty() != null) {
      eventPublisher.publishEvent(new BomChangedEvent(bom.getParentItem().getItemId()));
    }

    return BomDetailResponse.fromEntity(bom);
  }


  // BOM 관계 수정
  @Override
  @Transactional
  public BomDetailResponse updateBomRelation(final Long bomId,
      final UpdateBomRelationRequest request) {
    Bom bom = findBomById(bomId);

    Long oldParentItemId = bom.getParentItem().getItemId();
    Long newParentItemId = request.getNewParentItemId();
    Long childItemId = bom.getChildItem().getItemId();

    // 부모-자식 순환 참조 체크 (새로운 부모가 자식의 후손인 경우)
    if (bomClosureRepository.existsById_AncestorItemIdAndId_DescendantItemId(
        childItemId, newParentItemId)) {
      throw new CustomException(ErrorCode.BOM_CIRCULAR_REFERENCE);
    }

    Item newParentItem = itemService.findItemByIdWithLock(newParentItemId);
    bom.updateRelation(request, newParentItem);

    // 기존 관계의 클로저 삭제 후 새로운 관계의 클로저 업데이트
    bomClosureRepository.deleteByDescendantItemId(childItemId);
    updateBomClosure(newParentItemId, childItemId);

    // 기존 루트와 새로운 루트의 캐시를 모두 갱신
    eventPublisher.publishEvent(new BomChangedEvent(oldParentItemId));
    eventPublisher.publishEvent(new BomChangedEvent(newParentItemId));

    return BomDetailResponse.fromEntity(bom);
  }


  // BOM 삭제
  @Override
  @Transactional
  public void deleteBom(final Long bomId) {
    Bom bom = findBomById(bomId);

    Long parentId = bom.getParentItem().getItemId();
    Long childItemId = bom.getChildItem().getItemId();

    // 1. 자식 BOM 존재 여부 확인
    boolean hasChildren = bomRepository.existsByParentItem_ItemId(childItemId);
    if (hasChildren) {
      throw new CustomException(ErrorCode.BOM_DELETE_CONFLICT);
    }

    // 삭제실행
    bomRepository.delete(bom);

    // 클로저 테이블 갱신
    updateBomClosure(parentId, childItemId);

    // 캐시 갱신 이벤트 발행
    eventPublisher.publishEvent(new BomChangedEvent(parentId));
  }

  // BOM 강제 삭제
  @Override
  @Transactional
  public void forceDeleteBom(final Long bomId) {
    Bom bom = findBomById(bomId);

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


  //===================================================================================
  // 공통 메소드
  @Override
  @Transactional(readOnly = true)
  public Bom findBomById(final Long bomId) {
    return bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));
  }


  // BOM 관계 수정시 클로저 업데이트
  @Override
  @Transactional
  public void updateBomClosure(final Long parentId, final Long childId) {
    // 부모 ID에 대한 잠금 객체를 사용
    ReentrantLock lock = closureLocks.computeIfAbsent(parentId, k -> new ReentrantLock());

    lock.lock();
    try {
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
    } finally {
      lock.unlock();
      closureLocks.remove(parentId, lock);
    }
  }

  // BOM 선택한 품목 캐시 재생성 (Listener에서 호출됨)
  @Override
  @Transactional
  public void rebuildBomCostCache(final Long rootItemId) {
    bomCostCacheRepository.deleteByRootItemId(rootItemId);
    Item rootItem = itemService.findItemById(rootItemId);

    List<BomCostCache> caches = bomCacheBuilder.build(rootItem);
    bomCostCacheRepository.saveAll(caches);
  }

  // BOM 전체 캐시 재생성
  @Override
  @Transactional
  public void rebuildAllBomCache() {
    List<Long> allRootItemIds = bomRepository.findAllRootItemIds();
    for (Long rootItemId : allRootItemIds) {
      rebuildBomCostCache(rootItemId);
    }
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
      BigDecimal totalCost = newAccQty.multiply(unitCost);

      caches.add(BomCostCache.create(root.getItemId(), child.getItemId(), depth + 1,
          newAccQty, unitCost,totalCost));

      dfsBuildCache(root, child, newAccQty, depth + 1, caches);
    }
  }
}
