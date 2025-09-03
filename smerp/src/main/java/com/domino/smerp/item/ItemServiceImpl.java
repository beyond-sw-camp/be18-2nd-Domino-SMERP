package com.domino.smerp.item;

import com.domino.smerp.item.constants.ItemAct;
import com.domino.smerp.item.constants.SafetyStockAct;
import com.domino.smerp.item.dto.request.ItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemStatusRequest;
import com.domino.smerp.item.dto.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemStatusRepository itemStatusRepository;

    @Override
    @Transactional
    public ItemResponse createItem(final ItemRequest request) {
        ItemStatus itemStatus =
                itemStatusRepository.findById(request.getItemStatusId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("존재하지 않는 품목 구분(Item Status) ID입니다."));

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
        Item item = itemRepository.findById(itemId)
                                    .orElseThrow(
                                        () -> new IllegalArgumentException("ID " + itemId + "에 해당하는 품목을 찾을 수 없습니다."));
        return ItemResponse.fromEntity(item);
    }

    // 품목 수정(품목 구분 포함)
    @Override
    @Transactional
    public ItemResponse updateItem(final Long itemId, final ItemRequest request) {
        Item item = itemRepository.findById(itemId)
                                    .orElseThrow(
                                        () -> new IllegalArgumentException("ID " + itemId + "에 해당하는 품목을 찾을 수 없습니다."));

        ItemStatus itemStatus = null;
        if (request.getItemStatusId() != null) {
            itemStatus = itemStatusRepository.findById(request.getItemStatusId())
                                                .orElseThrow(
                                                    () -> new IllegalArgumentException("존재하지 않는 품목 구분(Item Status) ID입니다."));
        }

        item.updateItem(request, itemStatus);

        Item updatedItem = itemRepository.save(item);
        return ItemResponse.fromEntity(updatedItem);
    }

    // 품목 안전재고 / 사용여부 수정
    @Override
    @Transactional
    public ItemResponse updateItemStatus(final Long itemId, final UpdateItemStatusRequest request) {
        Item item = itemRepository.findById(itemId)
                                    .orElseThrow(
                                        () -> new IllegalArgumentException("ID " + itemId + "에 해당하는 품목을 찾을 수 없습니다."));

        item.updateStatus(request);

        Item updatedItem = itemRepository.save(item);
        return ItemResponse.fromEntity(updatedItem);
    }


    // 품목 삭제
    @Override
    @Transactional
    public void deleteItem(final Long itemId) {
        // 수불 이력이 없는 품목만 삭제 가능.
        // 이 부분에 실제 수불 이력을 확인하는 로직 추가 예정
        if (!itemRepository.existsById(itemId)) {
            throw new IllegalArgumentException("ID " + itemId + "에 해당하는 품목을 찾을 수 없습니다.");
        }
        itemRepository.deleteById(itemId);
    }

}