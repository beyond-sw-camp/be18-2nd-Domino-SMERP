package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.ItemRpoCrossedTable;
import com.domino.smerp.purchase.requestpurchaseorder.ItemRpoCrossedTableRepository;
import com.domino.smerp.purchase.requestpurchaseorder.ItemRpoCrossedTableService;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRpoCrossedTableServiceImpl implements ItemRpoCrossedTableService {

  private final ItemRpoCrossedTableRepository lineRepo;
  private final RequestPurchaseOrderRepository rpoRepo;

  @Override
  public ItemRpoCrossedTable addLine(Long rpoId, Long itemId, int qty) {
    RequestPurchaseOrder rpo =
        rpoRepo
            .findById(rpoId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid rpoId: " + rpoId));

    // 동일 품목이 이미 있는 경우 → qty 갱신
    ItemRpoCrossedTable line =
        lineRepo
            .findByRpo_IdAndItemId(rpoId, itemId)
            .orElseGet(
                () -> {
                  ItemRpoCrossedTable e = new ItemRpoCrossedTable();
                  e.setRpo(rpo);
                  e.setItemId(itemId);
                  return e;
                });

    line.setQty(qty);
    return lineRepo.save(line);
  }

  @Override
  public List<ItemRpoCrossedTable> getLines(Long rpoId) {
    return lineRepo.findByRpo_Id(rpoId);
  }

  @Override
  public long sumQty(Long rpoId) {
    return lineRepo.findByRpo_Id(rpoId).stream()
        .mapToLong(ItemRpoCrossedTable::getQty)
        .sum();
  }

  @Override
  public void removeLine(Long itemrpoId) {
    lineRepo.deleteById(itemrpoId);
  }
}
