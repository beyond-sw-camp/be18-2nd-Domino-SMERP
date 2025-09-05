package com.domino.smerp.item;

import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.constants.ItemAct;
import com.domino.smerp.item.constants.SafetyStockAct;
import com.domino.smerp.item.dto.request.CreateItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemStatusRequest;
import com.domino.smerp.item.dto.response.ItemResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
    public ItemResponse createItem(final CreateItemRequest request) {
        ItemStatus itemStatus = findItemStatusById(request.getItemStatusId());

        if(itemRepository.existsByRfid(request.getRfid())) {
            throw new CustomException(ErrorCode.DUPLICATE_RFID);
        }


        Item item = Item.builder()
            .itemStatus(itemStatus)
            .name(request.getName())
            .specification(request.getSpecification())
            .unit(request.getUnit())
            .inboundUnitPrice(request.getInboundUnitPrice())
            .outboundUnitPrice(request.getOutboundUnitPrice())
            .itemAct(Optional.ofNullable(request.getItemAct())
                .map(ItemAct::fromLabel)
                .orElse(ItemAct.ACTIVE))
            .safetyStock(Optional.ofNullable(request.getSafetyStock()).orElse(0))
            .safetyStockAct(Optional.ofNullable(request.getSafetyStockAct())
                .map(SafetyStockAct::fromLabel)
                .orElse(SafetyStockAct.DISABLED))
            .rfid(request.getRfid())
            .groupName1(request.getGroupName1())
            .groupName2(request.getGroupName2())
            .groupName3(request.getGroupName3())
            .build();

        Item savedItem = itemRepository.save(item);
        return ItemResponse.fromEntity(savedItem);
    }

    // 품목 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<ItemResponse> getItems() {

        return itemRepository.findAll()
            .stream()
            .map(ItemResponse::fromEntity)
            .toList();
    }

    // 품목 상세 조회
    @Override
    @Transactional(readOnly = true)
    public ItemResponse getItemById(final Long itemId) {
        Item item = findItemById(itemId);

        return ItemResponse.fromEntity(item);
    }

    // 품목 수정(품목 구분 포함)
    @Override
    @Transactional
    public ItemResponse updateItem(final Long itemId, final UpdateItemRequest request) {
        Item item = findItemById(itemId);

        ItemStatus itemStatus = null;
        if (request.getItemStatusId() != null) {
            itemStatus = findItemStatusById(request.getItemStatusId());
        }

        item.updateItem(request, itemStatus);

        Item updatedItem = itemRepository.save(item);
        return ItemResponse.fromEntity(updatedItem);
    }

    // 품목 안전재고 / 사용여부 수정
    @Override
    @Transactional
    public ItemResponse updateItemStatus(final Long itemId, final UpdateItemStatusRequest request) {
        Item item = findItemById(itemId);

        // 안전재고수량 음수 체크
        if (request.getSafetyStock() != null && request.getSafetyStock() < 0) {
            throw new CustomException(ErrorCode.INVALID_SAFETY_STOCK);
        }

        try {
            item.updateStatus(request);

            // 안전재고 / 사용여부 ENUM값 체크
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("ItemAct")) {
                throw new CustomException(ErrorCode.INVALID_ITEM_ACT);
            }
            if (e.getMessage().contains("SafetyStockAct")) {
                throw new CustomException(ErrorCode.INVALID_SAFETY_STOCK_ACT);
            }
            throw e;
        }

        Item updatedItem = itemRepository.save(item);
        return ItemResponse.fromEntity(updatedItem);
    }

    // 품목 삭제
    @Override
    @Transactional
    public void deleteItem(final Long itemId) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(
                () -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

        item.delete();
    }


    // findById 공통 메소드
    // 품목 구분 findById
    public ItemStatus findItemStatusById(final Long itemStatusId) {
        return itemStatusRepository.findById(itemStatusId)
            .orElseThrow(
                () -> new CustomException(ErrorCode.ITEM_STATUS_NOT_FOUND));
    }

    // 품목 findById
    public Item findItemById(final Long itemId) {
        return itemRepository.findById(itemId)
            .orElseThrow(
                () -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
    }

    // TODO: 유효성 검사 로직 추가 (e.g. null 여부)



}