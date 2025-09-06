package com.domino.smerp.item;

import com.domino.smerp.item.dto.request.ItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemStatusRequest;
import com.domino.smerp.item.dto.response.ItemResponse;
import com.domino.smerp.item.constants.ItemAct;
import java.util.List;


public interface ItemService {

  // 품목 생성
  ItemResponse createItem(ItemRequest request);

  // 품목 목록 조회
  List<ItemResponse> getItems();

  // 품목 상세 조회
  ItemResponse getItemById(Long itemId);

  // 품목 수정(품목 구분 포함)
  ItemResponse updateItem(Long itemId, ItemRequest request);

  // 품목 안전재고 / 사용여부 수정
  ItemResponse updateItemStatus(Long itemId, UpdateItemStatusRequest request);

  // 품목 삭제
  void deleteItem(Long itemId);
}