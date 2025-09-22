package com.domino.smerp.bom.controller;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.request.UpdateBomRelationRequest;
import com.domino.smerp.bom.dto.request.UpdateBomRequest;
import com.domino.smerp.bom.dto.response.BomCostCacheResponse;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.service.cache.BomCacheService;
import com.domino.smerp.bom.service.command.BomCommandService;
import com.domino.smerp.bom.service.query.BomQueryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boms")
@RequiredArgsConstructor
public class BomController {

  private final BomCacheService bomCacheService;
  private final BomCommandService bomCommandService;
  private final BomQueryService bomQueryService;

  // BOM 관계 생성
  @PostMapping
  public ResponseEntity<BomDetailResponse> createBom(
      final @Valid @RequestBody CreateBomRequest request) {
    return ResponseEntity.ok(bomCommandService.createBom(request));
  }

  // BOM 전체 목록 조회
  @GetMapping
  public ResponseEntity<List<BomListResponse>> getBoms() {
    return ResponseEntity.ok(bomQueryService.getBoms());
  }

  // BOM 품목구분 목록 조회
  @GetMapping("/item-status/{item-status-id}")
  public ResponseEntity<List<BomListResponse>> getBomsByItemStatusId(
      final @PathVariable("item-status-id") Long itemStatusId) {
    return ResponseEntity.ok(bomQueryService.getBomsByItemStatusId(itemStatusId));
  }

  // BOM 상세 조회
  @GetMapping("/{bom-id}")
  public ResponseEntity<BomDetailResponse> getBomDetail(final @PathVariable("bom-id") Long bomId,
      final @RequestParam(defaultValue = "inbound") String direction) {
    return ResponseEntity.ok(bomQueryService.getBomDetail(bomId, direction));
  }

  // 특정 품목의 BOM 조회 (직계 자식만)
  @GetMapping("/items/{parent-item-id}")
  public ResponseEntity<List<BomListResponse>> getBomByParentItemId(
      final @PathVariable("parent-item-id") Long parentItemId) {
    return ResponseEntity.ok(bomQueryService.getBomByParentItemId(parentItemId));
  }

  // 특정 품목의 BOM 조회 (모든 자식들)
  @GetMapping("/items/{item-id}/inbound")
  public ResponseEntity<List<BomListResponse>> getBomInbound(
      final @PathVariable("item-id") Long itemId) {
    return ResponseEntity.ok(bomQueryService.getBomInbound(itemId));
  }

  // BOM 소요량 산출
  @GetMapping("/items/{item-id}/requirements")
  public ResponseEntity<BomCostCacheResponse> calculateTotalQtyAndCost(
      final @PathVariable("item-id") Long itemId) {
    return ResponseEntity.ok(bomQueryService.calculateTotalQtyAndCost(itemId));
  }

  // BOM 수량/비고 수정
  @PatchMapping("/{bom-id}")
  public ResponseEntity<BomDetailResponse> updateBom(final @PathVariable("bom-id") Long bomId,
      final @RequestBody UpdateBomRequest request) {
    return ResponseEntity.ok(bomCommandService.updateBom(bomId, request));
  }

  // BOM 관계 수정
  @PatchMapping("/{bom-id}/relations")
  public ResponseEntity<BomDetailResponse> updateBomRelation(
      final @PathVariable("bom-id") Long bomId,
      final @RequestBody UpdateBomRelationRequest request) {
    return ResponseEntity.ok(bomCommandService.updateBomRelation(bomId, request));
  }


  // BOM 삭제
  @DeleteMapping("/{bom-id}")
  public ResponseEntity<Void> deleteBom(final @PathVariable("bom-id") Long bomId) {
    bomCommandService.deleteBom(bomId);
    return ResponseEntity.noContent().build();
  }

  // BOM 강제 삭제
  @DeleteMapping("/{bom-id}/force")
  public ResponseEntity<Void> deleteForceBom(final @PathVariable("bom-id") Long bomId) {
    bomCommandService.forceDeleteBom(bomId);
    return ResponseEntity.noContent().build();
  }

  // BOM 전체 캐시 재생성
  @PostMapping("/cache/rebuild")
  public ResponseEntity<Void> rebuildAllBomCache() {
    bomCacheService.rebuildAllBomCache();
    return ResponseEntity.ok().build();
  }

  // BOM 선택한 품목 캐시 재생성
  @PostMapping("/cache/refresh/{item-id}")
  public ResponseEntity<Void> refreshBomCache(final @PathVariable("item-id") Long itemId) {
    bomCacheService.rebuildBomCostCache(itemId);
    return ResponseEntity.ok().build();
  }
}