package com.domino.smerp.item;

import com.domino.smerp.item.dto.request.CreateItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemStatusRequest;
import com.domino.smerp.item.dto.response.ItemResponse;
import java.util.List;

public interface ItemService {

  // 품목 생성
  ItemResponse createItem(final CreateItemRequest request);

  // 품목 목록 조회
  List<ItemResponse> getItems();

  // 품목 상세 조회
  ItemResponse getItemById(final Long itemId);

  // 품목 수정(품목 구분 포함)
  ItemResponse updateItem(final Long itemId, final UpdateItemRequest request);

  // 품목 안전재고 / 사용여부 수정
  ItemResponse updateItemStatus(final Long itemId, final UpdateItemStatusRequest request);

  // 품목 삭제
  void deleteItem(final Long itemId);
}