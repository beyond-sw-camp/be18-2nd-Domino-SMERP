package com.domino.smerp.item;

public interface ItemManagementService {

  void deleteItemWithAllAssociations(final Long itemId);
}
