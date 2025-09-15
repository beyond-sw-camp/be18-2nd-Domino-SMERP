package com.domino.smerp.item.service;

import com.domino.smerp.lotno.service.LotNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemManagementServiceImpl implements ItemManagementService {
  private final ItemService itemService;
  private final LotNumberService lotNumberService;

  @Override
  @Transactional
  public void deleteItemWithAllAssociations(final Long itemId) {
    lotNumberService.softDeleteByItemId(itemId);

    itemService.softDeleteItem(itemId);
  }
}
