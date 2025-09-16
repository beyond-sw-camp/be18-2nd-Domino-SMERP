package com.domino.smerp.item.service;

public interface ItemManagementService {

  void deleteItemWithAllAssociations(final Long itemId);
}
