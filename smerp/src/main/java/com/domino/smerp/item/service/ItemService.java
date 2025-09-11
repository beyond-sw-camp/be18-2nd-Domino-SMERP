package com.domino.smerp.item.service;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.item.dto.request.CreateItemRequest;
import com.domino.smerp.item.dto.request.ItemSearchRequest;
import com.domino.smerp.item.dto.request.UpdateItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemStatusRequest;
import com.domino.smerp.item.dto.response.ItemDetailResponse;
import com.domino.smerp.item.dto.response.ItemListResponse;
import com.domino.smerp.item.dto.response.ItemStatusResponse;
import org.springframework.data.domain.Pageable;

public interface ItemService {

  // 품목 생성
  ItemDetailResponse createItem(final CreateItemRequest request);

  // 품목 목록 조회
  PageResponse<ItemListResponse> searchItems(final ItemSearchRequest keyword, final Pageable pageable);

  // 품목 상세 조회
  ItemDetailResponse getItemById(final Long itemId);

  // 품목 수정(품목 구분 포함)
  ItemDetailResponse updateItem(final Long itemId, final UpdateItemRequest request);

  // 품목 안전재고 / 사용여부 수정
  ItemStatusResponse updateItemStatus(final Long itemId, final UpdateItemStatusRequest request);

  // 품목 삭제
  void deleteItem(final Long itemId);
}