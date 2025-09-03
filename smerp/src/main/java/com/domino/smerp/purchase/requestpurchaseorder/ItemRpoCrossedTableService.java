package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.ItemRpoCrossedTable;
import java.util.List;

public interface ItemRpoCrossedTableService {

  ItemRpoCrossedTable addLine(Long rpoId, Long itemId, int qty);

  List<ItemRpoCrossedTable> getLines(Long rpoId);

  long sumQty(Long rpoId);

  void removeLine(Long itemrpoId);
}
