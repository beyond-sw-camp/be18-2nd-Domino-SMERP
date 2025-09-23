package com.domino.smerp.bom.service.command;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.request.UpdateBomRelationRequest;
import com.domino.smerp.bom.dto.request.UpdateBomRequest;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.bom.entity.BomClosure;
import com.domino.smerp.bom.event.BomChangedEvent;
import com.domino.smerp.bom.repository.BomClosureRepository;
import com.domino.smerp.bom.repository.BomRepository;
import com.domino.smerp.bom.service.cache.BomCacheBuilder;
import com.domino.smerp.bom.service.cache.BomCacheService;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemService;
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

  private final ItemService itemService;

  private final BomRepository bomRepository;
  private final BomClosureRepository bomClosureRepository;

  private final ApplicationEventPublisher eventPublisher;

  private static final ConcurrentHashMap<Long, ReentrantLock> closureLocks = new ConcurrentHashMap<>();


  // BOM 생성
  @Override
  @Transactional
  public BomDetailResponse createBom(final CreateBomRequest request) {
    // 중복 관계 방지
    if (bomRepository.existsByParentItem_ItemIdAndChildItem_ItemId(request.getParentItemId(),
        request.getChildItemId())) {
      throw new CustomException(ErrorCode.BOM_DUPLICATE_RELATIONSHIP);
    }
    // 순환 참조 방지
    if (bomClosureRepository.existsById_AncestorItemIdAndId_DescendantItemId(
        request.getChildItemId(), request.getParentItemId())) {
      throw new CustomException(ErrorCode.BOM_CIRCULAR_REFERENCE);
    }

    final Item parentItem;
    final Item childItem;

    // 품목 ID가 더 작은 쪽이 먼저 잠금 획득
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

    final Bom savedBom = bomRepository.save(Bom.create(request, parentItem, childItem));

    // 클로저 업데이트
    updateBomClosure(parentItem.getItemId(), childItem.getItemId());

    // 이벤트 발행 → Listener에서 캐시 갱신
    eventPublisher.publishEvent(new BomChangedEvent(findRootId(parentItem.getItemId())));

    return BomDetailResponse.fromEntity(savedBom);
  }

  // BOM 수정
  @Override
  @Transactional
  public BomDetailResponse updateBom(final Long bomId, final UpdateBomRequest request) {
    final Bom bom = findBomById(bomId);

    // 수량과 비고만 업데이트
    bom.update(request);

    if (request.getQty() != null) {
      final Long rootId = findRootId(bom.getParentItem().getItemId());
      eventPublisher.publishEvent(new BomChangedEvent(rootId));
    }

    return BomDetailResponse.fromEntity(bom);
  }


  // BOM 관계 수정
  @Override
  @Transactional
  public BomDetailResponse updateBomRelation(final Long bomId,
      final UpdateBomRelationRequest request) {
    final Bom bom = findBomById(bomId);

    final Long oldParentItemId = bom.getParentItem().getItemId();
    final Long newParentItemId = request.getNewParentItemId();
    final Long childItemId = bom.getChildItem().getItemId();

    // 부모-자식 순환 참조 체크 (새로운 부모가 자식의 후손인 경우)
    if (bomClosureRepository.existsById_AncestorItemIdAndId_DescendantItemId(
        childItemId, newParentItemId)) {
      throw new CustomException(ErrorCode.BOM_CIRCULAR_REFERENCE);
    }

    final Item newParentItem = itemService.findItemByIdWithLock(newParentItemId);
    bom.updateRelation(request, newParentItem);

    // 기존 관계의 클로저 삭제 후 새로운 관계의 클로저 업데이트
    bomClosureRepository.deleteByDescendantItemId(childItemId);
    updateBomClosure(newParentItemId, childItemId);

    // 기존 루트와 새로운 루트의 캐시를 모두 갱신
    final Long oldRootId = findRootId(oldParentItemId);
    final Long newRootId = findRootId(newParentItemId);
    eventPublisher.publishEvent(new BomChangedEvent(oldRootId));
    eventPublisher.publishEvent(new BomChangedEvent(newRootId));

    return BomDetailResponse.fromEntity(bom);
  }


  // BOM 삭제 (자식 있을 경우 불가)
  @Override
  @Transactional
  public void deleteBom(final Long bomId) {
    final Bom bom = findBomById(bomId);

    final Long parentId = bom.getParentItem().getItemId();
    final Long childItemId = bom.getChildItem().getItemId();

    // 1. 자식 BOM 존재 여부 확인
    final boolean hasChildren = bomRepository.existsByParentItem_ItemId(childItemId);
    if (hasChildren) {
      throw new CustomException(ErrorCode.BOM_DELETE_CONFLICT);
    }

    // 삭제실행
    bomRepository.delete(bom);

    // 클로저 테이블 갱신
    updateBomClosure(parentId, childItemId);

    // 캐시 갱신 이벤트 발행
    final Long rootId = findRootId(parentId);
    eventPublisher.publishEvent(new BomChangedEvent(rootId));
  }

  // BOM 강제 삭제
  @Override
  @Transactional
  public void forceDeleteBom(final Long bomId) {
    final Bom bom = findBomById(bomId);

    final Long targetItemId = bom.getChildItem().getItemId();

    final List<BomClosure> descendants = bomClosureRepository.findById_AncestorItemId(targetItemId);
    final List<Long> descendantItemIds = descendants.stream()
        .map(BomClosure::getDescendantItemId)
        .collect(Collectors.toList());

    bomRepository.deleteAllByChildItem_ItemIdIn(descendantItemIds);
    bomClosureRepository.deleteByDescendantItemId(targetItemId);

    // 상위 root 들 invalidate
    final List<BomClosure> ancestors = bomClosureRepository.findById_DescendantItemId(targetItemId);

    ancestors.stream()
        .map(BomClosure::getAncestorItemId)
        .map(this::findRootId)
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
    final ReentrantLock lock = closureLocks.computeIfAbsent(parentId, k -> new ReentrantLock());

    lock.lock();
    try {
      // 1. 자기 자신 노드 보장
      bomClosureRepository.upsertBomClosure(childId, childId, 0);
      bomClosureRepository.upsertBomClosure(parentId, parentId, 0);

      // 직계 관계 보장
      bomClosureRepository.upsertBomClosure(parentId, childId, 1);

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
      for (final BomClosure ancestor : ancestors) {
        for (final BomClosure descendant : descendants) {
          final int depth = ancestor.getDepth() + descendant.getDepth() + 1;

          // 이미 직계로 넣은 (parent, child, depth=1)은 건너뛰기
          if (ancestor.getAncestorItemId().equals(parentId) &&
              descendant.getDescendantItemId().equals(childId) &&
              depth == 1) {
            continue;
          }

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

  // BOM 생성 시 조상찾아서 캐시에 넘기기 용
  public Long findRootId(final Long itemId) {
    Long rootId = bomClosureRepository.findRootAncestorId(itemId);
    return rootId != null ? rootId : itemId; // 혹시 null이면 자기 자신
  }

}
