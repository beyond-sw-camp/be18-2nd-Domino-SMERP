package com.domino.smerp.bom.controller;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.response.BomDetailResponse;
import com.domino.smerp.bom.dto.response.BomListResponse;
import com.domino.smerp.bom.dto.response.CreateBomResponse;
import com.domino.smerp.bom.service.BomService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boms")
@RequiredArgsConstructor
public class BomController {

  private final BomService bomService;

  @PostMapping
  public ResponseEntity<CreateBomResponse> createBom(
      final @Valid @RequestBody CreateBomRequest request) {
    return ResponseEntity.ok(bomService.createBom(request));
  }

  // BOM 전체 목록 조회
  @GetMapping
  public ResponseEntity<List<BomListResponse>> getAllBoms() {
    // TODO: 서비스단에 findAll 구현 필요
    return ResponseEntity.ok(bomService.getAllBoms());
  }

  // BOM 상세 조회
  @GetMapping("/{bom-id}")
  public ResponseEntity<BomDetailResponse> getBomDetail(
      final @PathVariable("bom-id") Long bomId) {
    return ResponseEntity.ok(bomService.getBomDetail(bomId));
  }

  // 특정 품목의 BOM 조회
  @GetMapping("/items/{parent-item-id}")
  public ResponseEntity<List<BomListResponse>> getBomByParentItemId(
      final @PathVariable("parent-item-id") Long parentItemId) {
    return ResponseEntity.ok(bomService.getBomByParentItemId(parentItemId));
  }



// TODO: update, delete, calculate APIs 추가 예정


}
