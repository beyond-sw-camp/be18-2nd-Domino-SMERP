package com.domino.smerp.lotno.repository.command;

public interface LotNumberCommandRepository {
  void bulkSoftDeleteByItemId(final Long itemId);

}
