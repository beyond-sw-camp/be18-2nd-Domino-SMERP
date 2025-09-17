package com.domino.smerp.bom.controller;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.request.UpdateBomRequest;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.dto.response.BomRequirementResponse;
import com.domino.smerp.bom.dto.response.CreateBomResponse;
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

  private final BomCommandService bomCommandService;
  private final BomQueryService bomQueryService;

  // BOM 관계 생성
  @PostMapping
  public ResponseEntity<CreateBomResponse> createBom(
      final @Valid @RequestBody CreateBomRequest request) {
    return ResponseEntity.ok(bomCommandService.createBom(request));
  }

  // BOM 전체 목록 조회
  @GetMapping
  public ResponseEntity<List<BomListResponse>> getBoms() {
    return ResponseEntity.ok(bomQueryService.getBoms());
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
  @GetMapping("/items/{item-id}/descendants")
  public ResponseEntity<List<BomListResponse>> getBomDescendants(
      final @PathVariable("item-id") Long itemId) {
    return ResponseEntity.ok(bomQueryService.getBomDescendants(itemId));
  }

  // TODO: 소요량 산출 API
  @GetMapping("/items/{item-id}/requirements")
  public ResponseEntity<List<BomRequirementResponse>> calculateTotalQtyAndCost(
      final @PathVariable("item-id") Long itemId) {
    // 반환 DTO를 BomCostCache 대신 별도로 정의하는 것이 좋습니다.
    return ResponseEntity.ok(bomQueryService.calculateTotalQtyAndCost(itemId));
  }

  // TODO: 수량/비고 수정 API
  @PatchMapping("/{bom-id}")
  public ResponseEntity<BomDetailResponse> updateBom(final @PathVariable("bom-id") Long bomId,
      final @RequestBody UpdateBomRequest request) {
    return ResponseEntity.ok(bomCommandService.updateBom(bomId, request));
  }

  // TODO: BOM 삭제 API
  @DeleteMapping("/{bom-id}")
  public ResponseEntity<Void> deleteBom(final @PathVariable("bom-id") Long bomId) {
    bomCommandService.deleteBom(bomId);
    return ResponseEntity.noContent().build();
  }

  // TODO: BOM 강제 삭제 API
  @DeleteMapping("/{bom-id}/force")
  public ResponseEntity<Void> deleteForceBom(final @PathVariable("bom-id") Long bomId) {
    bomCommandService.deleteForceBom(bomId);
    return ResponseEntity.noContent().build();
  }


}
