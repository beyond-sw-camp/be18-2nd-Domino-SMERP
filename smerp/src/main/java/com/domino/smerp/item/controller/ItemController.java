package com.domino.smerp.item.controller;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.item.service.ItemManagementService;
import com.domino.smerp.item.service.ItemService;
import com.domino.smerp.item.dto.request.CreateItemRequest;
import com.domino.smerp.item.dto.request.ItemSearchRequest;
import com.domino.smerp.item.dto.request.UpdateItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemStatusRequest;
import com.domino.smerp.item.dto.response.ItemDetailResponse;
import com.domino.smerp.item.dto.response.ItemListResponse;
import com.domino.smerp.item.dto.response.ItemStatusResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;
  private final ItemManagementService itemManagementService;

  // 품목 생성
  @PostMapping
  public ResponseEntity<ItemDetailResponse> createItem(
      final @Valid @RequestBody CreateItemRequest request) {

    return ResponseEntity.ok(itemService.createItem(request));
  }

  // 품목 목록 조회 (검색조건은 쿼리파라미터로)
  @GetMapping
  public ResponseEntity<PageResponse<ItemListResponse>> getItems(
      @ModelAttribute ItemSearchRequest keyword,
      Pageable pageable) {
    return ResponseEntity.ok(itemService.searchItems(keyword, pageable));
  }

  // 품목 상세 조회
  @GetMapping("/{item-id}")
  public ResponseEntity<ItemDetailResponse> getItemById(
      @PathVariable("item-id") final Long itemId) {
    return ResponseEntity.ok(itemService.getItemById(itemId));
  }

  // 품목 수정
  @PatchMapping("/{item-id}")
  public ResponseEntity<ItemDetailResponse> updateItem(@PathVariable("item-id") final Long itemId,
      final @Valid @RequestBody UpdateItemRequest request) {
    return ResponseEntity.ok(itemService.updateItem(itemId, request));
  }

  // 품목 사용여부, 안전재고 수정
  @PatchMapping("/{item-id}/status")
  public ResponseEntity<ItemStatusResponse> updateItemStatus(
      @PathVariable("item-id") final Long itemId,
      final @Valid @RequestBody UpdateItemStatusRequest request) {
    return ResponseEntity.ok(itemService.updateItemStatus(itemId, request));
  }

  // 품목 삭제
  @DeleteMapping("/{item-id}")
  public ResponseEntity<Void> deleteItem(@PathVariable("item-id") final Long itemId) {
    // itemService.deleteItem(itemId);
    // itemService.softDeleteItem(itemId);
    itemManagementService.deleteItemWithAllAssociations(itemId);
    return ResponseEntity.noContent().build();
  }
}