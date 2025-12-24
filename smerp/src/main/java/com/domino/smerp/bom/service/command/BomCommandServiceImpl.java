package com.domino.smerp.bom.service.command;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.request.UpdateBomRelationRequest;
import com.domino.smerp.bom.dto.request.UpdateBomRequest;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.bom.entity.BomClosure;
import com.domino.smerp.bom.repository.BomClosureRepository;
import com.domino.smerp.bom.repository.BomRepository;
import com.domino.smerp.bom.support.BomReader;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemService;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BomCommandServiceImpl implements BomCommandService {

  private final ItemService itemService;

  private final BomRepository bomRepository;
  private final BomReader bomReader;
  private final BomClosureRepository bomClosureRepository;

  private static final ConcurrentHashMap<Long, ReentrantLock> closureLocks = new ConcurrentHashMap<>();


  // TODO: REBUILD 시점 정하기
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

    Item parentItem;
    Item childItem;

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

    Bom savedBom = bomRepository.save(Bom.create(request, parentItem, childItem));
    // 클로저 업데이트 (Upsert 기반으로 구조 일관성 유지)
    updateBomClosure(parentItem.getItemId(), childItem.getItemId());

    return BomDetailResponse.fromEntity(savedBom);
  }

  // BOM 수정
  @Override
  @Transactional
  public BomDetailResponse updateBom(final Long bomId, final UpdateBomRequest request) {
    final Bom bom = bomReader.findBomById(bomId);

    // 수량과 비고만 업데이트
    bom.update(request);

    return BomDetailResponse.fromEntity(bom);
  }


  // BOM 관계 수정
  @Override
  @Transactional
  public BomDetailResponse updateBomRelation(final Long bomId,
      final UpdateBomRelationRequest request) {

    final Bom bom = bomReader.findBomById(bomId);

    final Long newParentItemId = request.getNewParentItemId();
    final Long childItemId = bom.getChildItem().getItemId();

    // 부모-자식 순환 참조 체크 (새로운 부모가 자식의 후손인 경우)
    if (bomClosureRepository.existsById_AncestorItemIdAndId_DescendantItemId(
        childItemId, newParentItemId)) {
      throw new CustomException(ErrorCode.BOM_CIRCULAR_REFERENCE);
    }

    final List<Long> subtreeDescendants =
        bomClosureRepository.findById_AncestorItemId(childItemId)
            .stream()
            .map(BomClosure::getDescendantItemId)
            .toList();

    final List<Long> oldAncestors =
        bomClosureRepository.findById_DescendantItemId(childItemId)
            .stream()
            .map(BomClosure::getAncestorItemId)
            .filter(a -> !a.equals(childItemId))
            .toList();

    if (!oldAncestors.isEmpty() && !subtreeDescendants.isEmpty()) {
      bomClosureRepository.deleteSubtreeRelations(oldAncestors, subtreeDescendants);
    }

    final Item newParentItem = itemService.findItemByIdWithLock(newParentItemId);
    bom.updateRelation(request, newParentItem);

    // 기존 관계의 클로저 삭제 후 새로운 관계의 클로저 업데이트
    //bomClosureRepository.deleteByDescendantItemId(childItemId);
    updateBomClosure(newParentItemId, childItemId);

    return BomDetailResponse.fromEntity(bom);
  }


  // BOM 삭제 (자식 있을 경우 불가)
  @Override
  @Transactional
  public void deleteBom(final Long bomId) {
    final Bom bom = bomReader.findBomById(bomId);

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
  }

  // BOM 강제 삭제
  @Override
  @Transactional
  public void forceDeleteBom(final Long bomId) {
    final Bom bom = bomReader.findBomById(bomId);

    final Long targetItemId = bom.getChildItem().getItemId();

    final List<BomClosure> descendants = bomClosureRepository.findById_AncestorItemId(targetItemId);
    final List<Long> descendantItemIds = descendants.stream()
        .map(BomClosure::getDescendantItemId)
        .collect(Collectors.toList());

    bomRepository.deleteAllByChildItem_ItemIdIn(descendantItemIds);
    bomClosureRepository.deleteByDescendantItemId(targetItemId);

  }


  //===================================================================================
  // 헬퍼 메소드
  // BOM 관계 수정시 클로저 업데이트
  private void updateBomClosure(final Long parentId, final Long childId) {
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

}
