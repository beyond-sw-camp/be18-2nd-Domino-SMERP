package com.domino.smerp.item;

import com.domino.smerp.item.dto.request.CreateItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemStatusRequest;
import com.domino.smerp.item.dto.response.ItemDetailResponse;
import com.domino.smerp.item.dto.response.ItemListResponse;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @PostMapping
  public ResponseEntity<ItemDetailResponse> createItem(
      final @Valid @RequestBody CreateItemRequest request) {

    return ResponseEntity.ok(itemService.createItem(request));
  }

  @GetMapping
  public ResponseEntity<List<ItemListResponse>> getItems() {
    return ResponseEntity.ok(itemService.getItems());
  }

  @GetMapping("/{item-id}")
  public ResponseEntity<ItemDetailResponse> getItemById(
      @PathVariable("item-id") final Long itemId) {
    return ResponseEntity.ok(itemService.getItemById(itemId));
  }

  @PatchMapping("/{item-id}")
  public ResponseEntity<ItemDetailResponse> updateItem(@PathVariable("item-id") final Long itemId,
      final @Valid @RequestBody UpdateItemRequest request) {
    return ResponseEntity.ok(itemService.updateItem(itemId, request));
  }

  @PatchMapping("/{item-id}/status")
  public ResponseEntity<ItemDetailResponse> updateItemStatus(
      @PathVariable("item-id") final Long itemId,
      final @Valid @RequestBody UpdateItemStatusRequest request) {
    return ResponseEntity.ok(itemService.updateItemStatus(itemId, request));
  }

  @DeleteMapping("/{item-id}")
  public ResponseEntity<Void> deleteItem(@PathVariable("item-id") final Long itemId) {
    itemService.deleteItem(itemId);
    return ResponseEntity.noContent().build();
  }
}