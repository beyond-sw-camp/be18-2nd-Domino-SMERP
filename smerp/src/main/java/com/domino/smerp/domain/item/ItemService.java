package com.domino.smerp.domain.item;

import com.domino.smerp.domain.item.dto.ItemRequestDto;
import com.domino.smerp.domain.item.dto.ItemResponseDto;
import com.domino.smerp.domain.item.enums.ItemAct;
import java.util.List;


public interface ItemService {

  ItemResponseDto createItem(ItemRequestDto dto);

  List<ItemResponseDto> getItems();

  ItemResponseDto getItemById(Integer itemId);

  ItemResponseDto updateItem(Integer itemId, ItemRequestDto dto);

  ItemResponseDto updateItemStatus(Integer itemId, ItemRequestDto status);

  ItemResponseDto updateItemStatus(Integer itemId, ItemAct status);

  void deleteItem(Integer itemId);
}