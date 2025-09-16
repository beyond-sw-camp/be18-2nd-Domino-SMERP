package com.domino.smerp.item.service;

import com.domino.smerp.lotno.service.LotNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor  // REVIEW: 추후 ItemCommandServiceImpl로 변경 후 로직 리팩토링
public class ItemManagementServiceImpl implements ItemManagementService {

  private final ItemService itemService;
  private final LotNumberService lotNumberService;

  // 품목 삭제 시 자식도 소프트 딜리트
  @Override
  @Transactional
  public void deleteItemWithAllAssociations(final Long itemId) {
    lotNumberService.softDeleteByItemId(itemId);

    itemService.softDeleteItem(itemId);
  }
}
