package com.domino.smerp.item;

import com.domino.smerp.item.dto.request.ItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemStatusRequest;
import com.domino.smerp.item.dto.response.ItemResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @PostMapping
  public ResponseEntity<ItemResponse> createItem(@RequestBody ItemRequest request) {
    return ResponseEntity.ok(itemService.createItem(request));
  }

  @GetMapping
  public ResponseEntity<List<ItemResponse>> getItems() {
    return ResponseEntity.ok(itemService.getItems());
  }

  @GetMapping("/{item-id}")
  public ResponseEntity<ItemResponse> getItemById(@PathVariable("item-id") Long itemId) {
    return ResponseEntity.ok(itemService.getItemById(itemId));
  }

  @PatchMapping("/{item-id}")
  public ResponseEntity<ItemResponse> updateItem(@PathVariable("item-id") Long itemId,
      @RequestBody ItemRequest request) {
    return ResponseEntity.ok(itemService.updateItem(itemId, request));
  }

  @PatchMapping("/{item-id}/status")
  public ResponseEntity<ItemResponse> updateItemStatus(@PathVariable("item-id") Long itemId,
      @RequestBody UpdateItemStatusRequest request) {
    return ResponseEntity.ok(itemService.updateItemStatus(itemId, request));
  }

  @DeleteMapping("/{item-id}")
  public ResponseEntity<Void> deleteItem(@PathVariable("item-id") Long itemId) {
    itemService.deleteItem(itemId);
    return ResponseEntity.noContent().build();
  }
}
