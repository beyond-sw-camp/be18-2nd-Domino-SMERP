package com.domino.smerp.item;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.dto.request.CreateItemRequest;
import com.domino.smerp.item.dto.request.SearchItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemStatusRequest;
import com.domino.smerp.item.dto.response.ItemDetailResponse;
import com.domino.smerp.item.dto.response.ItemListResponse;
import com.domino.smerp.item.dto.response.ItemStatusResponse;
import com.domino.smerp.item.repository.ItemRepository;
import com.domino.smerp.item.repository.ItemStatusRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

  private final ItemRepository itemRepository;
  private final ItemStatusRepository itemStatusRepository;

  // 품목 생성
  @Override
  @Transactional
  public ItemDetailResponse createItem(final CreateItemRequest request) {
    ItemStatus itemStatus = findItemStatusById(request.getItemStatusId());

    // 중복 검사
    if (itemRepository.existsByName(request.getName())) {
      throw new CustomException(ErrorCode.DUPLICATE_ITEM);
    }
    if (itemRepository.existsByRfid(request.getRfid())) {
      throw new CustomException(ErrorCode.DUPLICATE_RFID);
    }

    // TODO: 품목 코드 로직 반영
    // String itemCode = generateItemCode(request.getName());

    Item item = Item.create(request, itemStatus);
    Item savedItem = itemRepository.save(item);

    return ItemDetailResponse.fromEntity(savedItem);
  }

  // 품목 목록 조회
  @Override
  @Transactional(readOnly = true)
  public PageResponse<ItemListResponse> searchItems(final SearchItemRequest keyword,
      final Pageable pageable) {
    return PageResponse.from(
        itemRepository.searchItems(keyword, pageable).map(ItemListResponse::fromEntity));
  }


  // 품목 상세 조회
  @Override
  @Transactional(readOnly = true)
  public ItemDetailResponse getItemById(final Long itemId) {
    Item item = findItemById(itemId);

    return ItemDetailResponse.fromEntity(item);
  }

  // 품목 수정(품목 구분 포함)
  @Override
  @Transactional
  public ItemDetailResponse updateItem(final Long itemId, final UpdateItemRequest request) {
    Item item = findItemById(itemId);

    ItemStatus itemStatus = null;
    if (request.getItemStatusId() != null) {
      itemStatus = findItemStatusById(request.getItemStatusId());
    }

    item.updateItem(request, itemStatus);

    return ItemDetailResponse.fromEntity(item);
  }

  // 품목 안전재고 / 사용여부 수정
  @Override
  @Transactional
  public ItemStatusResponse updateItemStatus(final Long itemId,
      final UpdateItemStatusRequest request) {
    Item item = findItemById(itemId);

    // 안전재고수량 음수 체크
    if (request.getSafetyStock() != null
        && request.getSafetyStock().compareTo(BigDecimal.ZERO) < 0) {
      throw new CustomException(ErrorCode.INVALID_SAFETY_STOCK);
    }

    item.updateStatus(request);

    return ItemStatusResponse.fromEntity(item);
  }

  // 품목 삭제
  @Override
  @Transactional
  public void deleteItem(final Long itemId) {
    Item item = findItemById(itemId);

    item.delete();
  }

  // 품목 소프트딜리트
  @Override
  @Transactional
  public void softDeleteItem(final Long itemId) {
    final Item item = findItemById(itemId);
    item.delete();
  }


  // findById 공통 메소드
  // 품목 구분 findById
  @Override
  @Transactional(readOnly = true)
  public ItemStatus findItemStatusById(final Long itemStatusId) {
    return itemStatusRepository.findById(itemStatusId)
        .orElseThrow(() -> new CustomException(ErrorCode.ITEM_STATUS_NOT_FOUND));
  }

  // 품목 findById
  @Override
  @Transactional(readOnly = true)
  public Item findItemById(final Long itemId) {
    return itemRepository.findById(itemId)
        .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
  }


  // 품목 비관적 락 findById
  @Override
  @Transactional
  public Item findItemByIdWithLock(final Long itemId) {
    return itemRepository.findByIdWithPessimisticLock(itemId)
        .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
  }



  // 품목 코드 로직
  // REVIEW: 품목명 한글인 경우 어떻게 할 지?, 품목코드 명명 규칙 정해진게 있을까요?
  /*
  private String generateItemCode(final String name) {
    // 1. 품목명에서 영문+숫자만 남기고 대문자로 변환
    //    예: "Threadlocker 243" → "THREADLOCKER243"
    String code = name.toUpperCase().replaceAll("[^A-Z0-9]", "");

    // 2. 최소 4자리 확보 (길이가 4 미만이면 뒤에 '0'을 채움)
    //    예: "AA" → "AA00"
    if (code.length() < 4) {
      code = String.format("%-4s", code).replace(' ', '0');
    }

    // 3. 앞 4자리만 잘라서 prefix로 사용
    //    예: "THREADLOCKER243" → "THRE"
    String prefix = code.substring(0, 4);

    // 4. DB에서 해당 prefix(앞글자)로 시작하는 item_code 개수 조회
    //    예: "THRE%" 로 시작하는 데이터가 5개 있으면 count = 5
    long count = itemRepository.countByItemCodeStartingWith(prefix);

    // 5. 검증: prefix별 item_code는 최대 999개까지만 생성 가능
    if (count >= 999) {
      throw new CustomException(ErrorCode.ITEM_CODE_LIMIT_EXCEEDED);
    }

    // 6. 최종 item_code 생성 (세 자리 일련번호, 001부터 시작)
    //    예: count=5 → "THRE-006"
    return String.format("%s-%03d", prefix, count + 1);
  }
  */

  // TODO: 유효성 검사 로직 추가 (e.g. null 여부)


}