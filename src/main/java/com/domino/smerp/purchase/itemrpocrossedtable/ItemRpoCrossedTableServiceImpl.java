package com.domino.smerp.purchase.itemrpocrossedtable;

import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemRepository;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemRpoCrossedTableServiceImpl implements ItemRpoCrossedTableService {

  private final ItemRpoCrossedTableRepository itemRpoCrossedTableRepository;
  private final RequestPurchaseOrderRepository requestPurchaseOrderRepository;
  private final ItemRepository itemRepository;

  @Override
  @Transactional
  public ItemRpoCrossedTable create(Long rpoId, Long itemId, int qty) {
    RequestPurchaseOrder rpo = requestPurchaseOrderRepository.findById(rpoId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + rpoId));

    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다. id=" + itemId));

    ItemRpoCrossedTable entity = ItemRpoCrossedTable.builder()
        .requestPurchaseOrder(rpo)
        .item(item)
        .qty(qty)
        .build();

    return itemRpoCrossedTableRepository.save(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ItemRpoCrossedTable> getByRpoId(Long rpoId) {
    return itemRpoCrossedTableRepository.findByRequestPurchaseOrderRpoId(rpoId);
  }

  @Override
  @Transactional
  public ItemRpoCrossedTable updateQty(Long lineId, int qty) {
    ItemRpoCrossedTable entity = itemRpoCrossedTableRepository.findById(lineId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청 라인을 찾을 수 없습니다. id=" + lineId));

    entity.updateQty(qty);
    return entity;
  }

  @Override
  @Transactional
  public void delete(Long lineId) {
    ItemRpoCrossedTable entity = itemRpoCrossedTableRepository.findById(lineId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청 라인을 찾을 수 없습니다. id=" + lineId));

    itemRpoCrossedTableRepository.delete(entity);
  }
}
