package com.domino.smerp.domain.item;

import com.domino.smerp.domain.item.dto.ItemRequestDto;
import com.domino.smerp.domain.item.dto.ItemResponseDto;
import com.domino.smerp.domain.item.enums.ItemAct;
import com.domino.smerp.domain.item.enums.SafetyStockAct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

  private final ItemRepository itemRepository;
  private final ItemStatusRepository itemStatusRepository;

  @Autowired
  public ItemServiceImpl(ItemRepository itemRepository, ItemStatusRepository itemStatusRepository) {
    this.itemRepository = itemRepository;
    this.itemStatusRepository = itemStatusRepository;
  }

  @Override
  public ItemResponseDto createItem(ItemRequestDto dto) {
    ItemStatus itemStatus = itemStatusRepository.findById(dto.getItemStatusId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 품목 구분(Item Status) ID입니다."));

    Item item = Item.builder()
        .itemStatus(itemStatus)
        .name(dto.getName())
        .specification(dto.getSpecification())
        .unit(dto.getUnit())
        .inboundUnitPrice(dto.getInboundUnitPrice())
        .outboundUnitPrice(dto.getOutboundUnitPrice())
        .itemAct(Optional.ofNullable(dto.getItemAct()).orElse(ItemAct.ACTIVE)) // 기본값 설정
        .safetyStock(Optional.ofNullable(dto.getSafetyStock()).orElse(0)) // 기본값 설정
        .safetyStockAct(
            Optional.ofNullable(dto.getSafetyStockAct()).orElse(SafetyStockAct.DISABLED)) // 기본값 설정
        .rfid(dto.getRfid())
        .groupName1(dto.getGroupName1())
        .groupName2(dto.getGroupName2())
        .groupName3(dto.getGroupName3())
        .build();

    Item savedItem = itemRepository.save(item);
    return mapToResponseDto(savedItem);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ItemResponseDto> getItems() {
    return itemRepository.findAll().stream()
        .map(item -> ItemResponseDto.builder()
            .itemId(item.getItemId())
            .itemStatusId(item.getItemStatus().getItemStatusId())
            .name(item.getName())
            .specification(item.getSpecification())
            .unit(item.getUnit())
            .inboundUnitPrice(item.getInboundUnitPrice())
            .outboundUnitPrice(item.getOutboundUnitPrice())
            .createdDate(item.getCreatedDate())
            .updatedDate(item.getUpdatedDate())
            .itemAct(item.getItemAct().name())
            .safetyStock(item.getSafetyStock())
            .safetyStockAct(item.getSafetyStockAct().name())
            .rfid(item.getRfid())
            .groupName1(item.getGroupName1())
            .groupName2(item.getGroupName2())
            .groupName3(item.getGroupName3())
            .build()
        )
        .toList();
  }


  @Override
  @Transactional(readOnly = true)
  public ItemResponseDto getItemById(Integer itemId) {
    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new IllegalArgumentException("ID " + itemId + "에 해당하는 품목을 찾을 수 없습니다."));
    return mapToResponseDto(item);
  }

  @Override
  public ItemResponseDto updateItem(Integer itemId, ItemRequestDto dto) {
    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new IllegalArgumentException("ID " + itemId + "에 해당하는 품목을 찾을 수 없습니다."));

    ItemStatus itemStatus = null;
    if (dto.getItemStatusId() != null) {
      itemStatus = itemStatusRepository.findById(dto.getItemStatusId())
          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 품목 구분(Item Status) ID입니다."));
    }

    // Setter 대신 전용 메서드를 사용해 데이터를 수정합니다.
    item.updateDetails(dto, itemStatus);

    Item updatedItem = itemRepository.save(item);
    return mapToResponseDto(updatedItem);
  }

  @Override
  public ItemResponseDto updateItemStatus(Integer itemId, ItemRequestDto status) {
    return null;
  }

  @Override
  public ItemResponseDto updateItemStatus(Integer itemId, ItemAct status) {
    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new IllegalArgumentException("ID " + itemId + "에 해당하는 품목을 찾을 수 없습니다."));

    item.updateStatus(status);

    Item updatedItem = itemRepository.save(item);
    return mapToResponseDto(updatedItem);
  }

  @Override
  public void deleteItem(Integer itemId) {
    // 요구사항 ITM-003: 수불 이력이 없는 품목만 삭제 가능.
    // 이 부분에 실제 수불 이력을 확인하는 로직이 필요합니다.
    // 예: if (stockMovementRepository.existsByItemId(itemId)) { throw new IllegalStateException(...) }
    // 현재는 로직 없이 단순 삭제만 구현합니다.
    if (!itemRepository.existsById(itemId)) {
      throw new IllegalArgumentException("ID " + itemId + "에 해당하는 품목을 찾을 수 없습니다.");
    }
    itemRepository.deleteById(itemId);
  }

  // 엔티티를 DTO로 변환하는 헬퍼 메서드
  private ItemResponseDto mapToResponseDto(Item item) {
    return ItemResponseDto.builder()
        .itemId(item.getItemId())
        .itemStatusId(item.getItemStatus().getItemStatusId())
        .name(item.getName())
        .specification(item.getSpecification())
        .unit(item.getUnit())
        .inboundUnitPrice(item.getInboundUnitPrice())
        .outboundUnitPrice(item.getOutboundUnitPrice())
        .createdDate(item.getCreatedDate())
        .updatedDate(item.getUpdatedDate())
        .itemAct(item.getItemAct().getDescription())
        .safetyStock(item.getSafetyStock())
        .safetyStockAct(item.getSafetyStockAct().getDescription())
        .rfid(item.getRfid())
        .groupName1(item.getGroupName1())
        .groupName2(item.getGroupName2())
        .groupName3(item.getGroupName3())
        .build();
  }
}
