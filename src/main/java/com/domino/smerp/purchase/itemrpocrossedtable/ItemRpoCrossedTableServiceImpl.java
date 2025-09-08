package com.domino.smerp.purchase.itemrpocrossedtable;

import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemRepository;
import com.domino.smerp.purchase.itemrpocrossedtable.dto.request.ItemRpoCrossedTableRequest;
import com.domino.smerp.purchase.itemrpocrossedtable.dto.response.ItemRpoCrossedTableResponse;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRpoCrossedTableServiceImpl implements ItemRpoCrossedTableService {

  private final ItemRpoCrossedTableRepository itemRpoCrossedTableRepository;
  private final RequestPurchaseOrderRepository requestPurchaseOrderRepository;
  private final ItemRepository itemRepository;

  @Override
  public ItemRpoCrossedTableResponse addLine(final Long rpoId,
      final ItemRpoCrossedTableRequest request) {
    RequestPurchaseOrder rpo = requestPurchaseOrderRepository.findById(rpoId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + rpoId));

    Item item = itemRepository.findById(request.getItemId())
        .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다. id=" + request.getItemId()));

    ItemRpoCrossedTable entity = ItemRpoCrossedTable.builder()
        .requestPurchaseOrder(rpo)
        .item(item)
        .qty(request.getQty())
        .build();

    return ItemRpoCrossedTableResponse.from(itemRpoCrossedTableRepository.save(entity));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ItemRpoCrossedTableResponse> getLinesByRpoId(final Long rpoId) {
    return itemRpoCrossedTableRepository.findByRequestPurchaseOrder_RpoId(rpoId)
        .stream()
        .map(ItemRpoCrossedTableResponse::from)
        .toList();
  }

  @Override
  public ItemRpoCrossedTableResponse updateLine(final Long rpoId, final Long lineId,
      final ItemRpoCrossedTableRequest request) {
    ItemRpoCrossedTable entity = itemRpoCrossedTableRepository.findById(lineId)
        .orElseThrow(() -> new EntityNotFoundException("라인을 찾을 수 없습니다. id=" + lineId));

    if (!entity.getRequestPurchaseOrder().getRpoId().equals(rpoId)) {
      throw new IllegalArgumentException("해당 RPO에 속하지 않는 라인입니다.");
    }

    entity.updateQty(request.getQty());
    return ItemRpoCrossedTableResponse.from(entity);
  }

  @Override
  public void deleteLine(final Long rpoId, final Long lineId) {
    ItemRpoCrossedTable entity = itemRpoCrossedTableRepository.findById(lineId)
        .orElseThrow(() -> new EntityNotFoundException("라인을 찾을 수 없습니다. id=" + lineId));

    if (!entity.getRequestPurchaseOrder().getRpoId().equals(rpoId)) {
      throw new IllegalArgumentException("해당 RPO에 속하지 않는 라인입니다.");
    }

    itemRpoCrossedTableRepository.delete(entity);
  }
}
