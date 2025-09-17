package com.domino.smerp.bom.service.command;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.request.UpdateBomRequest;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.CreateBomResponse;
import com.domino.smerp.bom.entity.Bom;
import com.domino.smerp.bom.entity.BomClosure;
import com.domino.smerp.bom.entity.BomCostCache;
import com.domino.smerp.bom.repository.BomRepository;
import com.domino.smerp.bom.repository.BomClosureRepository;
import com.domino.smerp.bom.repository.BomCostCacheRepository;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BomCommandServiceImpl implements BomCommandService {

  private final BomRepository bomRepository;
  private final BomClosureRepository bomClosureRepository;
  private final BomCostCacheRepository bomCostCacheRepository;
  private final ItemService itemService;

  // BOM 생성
  @Override
  @Transactional
  public CreateBomResponse createBom(final CreateBomRequest request) {
    Item parentItem = itemService.findItemById(request.getParentItemId());
    Item childItem = itemService.findItemById(request.getChildItemId());

    // 이 부분에서 Bom.create()로 parentBomId 필드를 제거했으니 기존 코드를 수정해야 함.
    // 기존 코드: Bom newBom = Bom.create(request, parentItem, childItem);
    Bom newBom = Bom.create(request, parentItem, childItem);

    // 중복 BOM 관계 체크 (optional)
    if (bomRepository.existsByParentItem_ItemIdAndChildItem_ItemId(parentItem.getItemId(),
        childItem.getItemId())) {
      throw new CustomException(ErrorCode.BOM_DUPLICATE_RELATIONSHIP);
    }

    Bom savedBom = bomRepository.save(newBom);
    updateBomClosure(parentItem.getItemId(), childItem.getItemId());
    rebuildBomCostCache(parentItem.getItemId());

    return CreateBomResponse.fromEntity(savedBom);
  }

  // BOM 수정 로직
  @Override
  @Transactional
  public BomDetailResponse updateBom(final Long bomId, final UpdateBomRequest request) {
    Bom bom = bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));

    bom.update(request); // update 메서드는 Bom 엔티티에 구현 필요

    // 수정된 부모/자식 관계가 있을 경우, 클로저 테이블 갱신
    Long oldParentItemId = bom.getParentItem().getItemId();
    Long newParentItemId = request.getParentItemId(); // UpdateBomRequest에 새로운 부모 아이디 필드 추가 필요

    // 관계가 변경된 경우에만 클로저 테이블 갱신
    if (!oldParentItemId.equals(newParentItemId)) {
      updateBomClosure(newParentItemId, bom.getChildItem().getItemId());
      // 기존 부모의 캐시도 갱신
      rebuildBomCostCache(oldParentItemId);
    }

    // 소요량(qty)만 변경된 경우
    if (request.getQty() != null) {
      rebuildBomCostCache(bom.getParentItem().getItemId());
    }

    return BomDetailResponse.fromEntity(bom);
  }

  // BOM 삭제 로직 (단일 관계 삭제)
  @Override
  @Transactional
  public void deleteBom(final Long bomId) {
    Bom bom = bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));

    Long parentId = bom.getParentItem().getItemId();

    // 1. Bom 테이블에서 관계 삭제
    bomRepository.delete(bom);

    // 2. BomClosure, BomCostCache 갱신
    updateBomClosure(parentId, bom.getChildItem().getItemId());
    rebuildBomCostCache(parentId);
  }

  // BOM 강제 삭제 로직 (하위 전체 삭제)
  @Override
  @Transactional
  public void deleteForceBom(final Long bomId) {
    Bom bom = bomRepository.findById(bomId)
        .orElseThrow(() -> new CustomException(ErrorCode.BOM_NOT_FOUND));

    Long targetItemId = bom.getChildItem().getItemId();

    // 1. BomClosure에서 하위 자손 관계를 찾아 ID 목록 조회
    List<BomClosure> descendants = bomClosureRepository.findById_AncestorItemId(targetItemId);
    List<Long> descendantItemIds = descendants.stream().map(BomClosure::getDescendantItemId)
        .collect(Collectors.toList());

    // 2. Bom 테이블에서 하위 BOM 관계 모두 삭제
    bomRepository.deleteAllByChildItem_ItemIdIn(
        descendantItemIds); // deleteAllByChildItemItemIdIn 메서드는 JpaRepository에 추가 필요

    // 3. BomClosure, BomCostCache에서 관련 캐시 모두 삭제
    bomClosureRepository.deleteByDescendantItemId(targetItemId);
    bomCostCacheRepository.deleteByRootItemId(targetItemId);
  }

  // BOM 관계 수정시 계층 재계산
  @Override
  @Transactional
  public void updateBomClosure(final Long parentId, final Long childId) {
    // 1. 기존 관계 삭제 (해당 자식 품목의 모든 경로 삭제)
    bomClosureRepository.deleteByDescendantItemId(childId);

    // 2. 새로운 관계 생성 및 삽입
    bomClosureRepository.save(BomClosure.create(childId, childId, 0)); // 자기 자신 관계

    // 모든 조상으로부터 새로운 자손 관계 생성
    List<BomClosure> closures = new ArrayList<>();
    List<BomClosure> ancestors = bomClosureRepository.findById_DescendantItemId(parentId);
    if (ancestors.isEmpty()) { // 최상위 품목인 경우
      ancestors.add(BomClosure.create(parentId, parentId, 0));
    }

    List<BomClosure> descendants = bomClosureRepository.findById_AncestorItemId(childId);

    for (BomClosure ancestor : ancestors) {
      for (BomClosure descendant : descendants) {
        closures.add(
            BomClosure.create(ancestor.getAncestorItemId(), descendant.getDescendantItemId(),
                ancestor.getDepth() + descendant.getDepth() + 1));
      }
    }
    bomClosureRepository.saveAll(closures);
  }

  // BOM 캐시 재생성
  @Override
  @Transactional
  public void rebuildBomCostCache(final Long rootItemId) {
    // 1. 기존 캐시 삭제
    bomCostCacheRepository.deleteByRootItemId(rootItemId);

    // 2. 루트 품목 조회
    Item rootItem = itemService.findItemById(rootItemId);

    // 3. DFS 탐색 시작 (자기 자신 제외)
    List<BomCostCache> caches = new ArrayList<>();
    dfsBuildCache(rootItem, rootItem, BigDecimal.ONE, 0, caches);

    // 4. 캐시 일괄 저장
    bomCostCacheRepository.saveAll(caches);
  }

  /**
   * DFS로 BOM 트리를 순회하며 누적 qty와 원가를 계산
   *
   * @param root    루트 Item (고정)
   * @param current 현재 Item
   * @param accQty  지금까지의 누적 수량
   * @param depth   계층 깊이
   * @param caches  결과 캐시 리스트
   */
  private void dfsBuildCache(final Item root, final Item current, final BigDecimal accQty,
      final int depth, final List<BomCostCache> caches) {
    // 현재 노드의 직계 자식 BOM 관계 조회
    List<Bom> children = bomRepository.findByParentItem_ItemId(current.getItemId());

    for (Bom childBom : children) {
      Item child = childBom.getChildItem();

      // 누적 수량 = 지금까지 accQty * 현재 BOM의 qty
      BigDecimal newAccQty = accQty.multiply(childBom.getQty());

      // 단가 = 자식 Item inbound 단가
      BigDecimal unitCost = child.getInboundUnitPrice();

      // 캐시 생성 (루트 기준으로 저장)
      BomCostCache cache = BomCostCache.create(root.getItemId(), child.getItemId(), depth + 1,
          newAccQty, unitCost);
      caches.add(cache);

      // 재귀적으로 하위 자식 탐색
      dfsBuildCache(root, child, newAccQty, depth + 1, caches);
    }
  }


}