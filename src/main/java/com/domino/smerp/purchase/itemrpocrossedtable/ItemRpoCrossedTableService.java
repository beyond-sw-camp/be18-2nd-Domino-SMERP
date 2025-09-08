package com.domino.smerp.purchase.itemrpocrossedtable;

import java.util.List;

public interface ItemRpoCrossedTableService {

  ItemRpoCrossedTable create(Long rpoId, Long itemId, int qty);

  List<ItemRpoCrossedTable> getByRpoId(Long rpoId);

  ItemRpoCrossedTable updateQty(Long lineId, int qty);

  void delete(Long lineId);
}
