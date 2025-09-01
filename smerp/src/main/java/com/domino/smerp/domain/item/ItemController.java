package com.domino.smerp.domain.item;

import com.domino.smerp.domain.item.dto.ItemRequestDto;
import com.domino.smerp.domain.item.dto.ItemResponseDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ItemController {

  private final ItemService itemService;

  @Autowired
  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }

  @PostMapping
  public ResponseEntity<ItemResponseDto> createItem(@RequestBody ItemRequestDto dto) {
    return ResponseEntity.ok(itemService.createItem(dto));
  }

  @GetMapping
  public ResponseEntity<List<ItemResponseDto>> getItems() {
    return ResponseEntity.ok(itemService.getItems());
  }

  @GetMapping("/{item-id}")
  public ResponseEntity<ItemResponseDto> getItemById(@PathVariable("item-id") Integer itemId) {
    return ResponseEntity.ok(itemService.getItemById(itemId));
  }

  @PatchMapping("/{item-id}")
  public ResponseEntity<ItemResponseDto> updateItem(@PathVariable("item-id") Integer itemId,
      @RequestBody ItemRequestDto dto) {
    return ResponseEntity.ok(itemService.updateItem(itemId, dto));
  }

  @PatchMapping("/{item-id}/status")
  public ResponseEntity<ItemResponseDto> updateItemStatus(@PathVariable("item-id") Integer itemId,
      @RequestBody ItemRequestDto dto) {
    return ResponseEntity.ok(itemService.updateItemStatus(itemId, dto));
  }


  @DeleteMapping("/{item-id}")
  public ResponseEntity<Void> deleteItem(@PathVariable("item-id") Integer itemId) {
    itemService.deleteItem(itemId);
    return ResponseEntity.noContent().build();
  }
}
