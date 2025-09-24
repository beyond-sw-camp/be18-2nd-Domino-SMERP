package com.domino.smerp.item;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.item.constants.ItemAct;
import com.domino.smerp.item.dto.request.CreateItemRequest;
import com.domino.smerp.item.dto.request.SearchItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemStatusRequest;
import com.domino.smerp.item.dto.response.ItemDetailResponse;
import com.domino.smerp.item.dto.response.ItemListResponse;
import com.domino.smerp.item.dto.response.ItemStatusResponse;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ItemService {

  // 품목 생성
  ItemDetailResponse createItem(final CreateItemRequest request);

  // 품목 목록 조회
  PageResponse<ItemListResponse> searchItems(final SearchItemRequest keyword,
      final Pageable pageable);

  // 품목 상세 조회
  ItemDetailResponse getItemById(final Long itemId);

  // 품목 수정(품목 구분 포함)
  ItemDetailResponse updateItem(final Long itemId, final UpdateItemRequest request);

  // 품목 안전재고 / 사용여부 수정
  ItemStatusResponse updateItemStatus(final Long itemId, final UpdateItemStatusRequest request);

  // 품목 삭제
  void deleteItem(final Long itemId);

  void softDeleteItem(final Long itemId);

  // 공통 메소드
  ItemStatus findItemStatusById(final Long itemStatusId);

  Item findItemById(final Long itemId);

  // 비관적 락
  Item findItemByIdWithLock(final Long itemId);

  List<Item> findItemByStatus(final ItemStatus itemStatus);

  Item findItemByName(final String name);

  Item findItemByRfid(final String rfid);

  List<Item> findItemByItemAct(final ItemAct itemAct);

  List<Item> findItemsBySafetyStockLessThan(final BigDecimal value);

  List<Item> findItemsBySafetyStockGreaterThan(final BigDecimal value);

}