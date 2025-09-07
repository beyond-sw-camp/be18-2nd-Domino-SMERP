package com.domino.smerp.purchase.itemrocrossedtable;

import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemRepository;
import com.domino.smerp.purchase.requestorder.RequestOrder;
import com.domino.smerp.purchase.requestorder.RequestOrderRepository;
import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderRequest;
import com.domino.smerp.purchase.requestorder.dto.response.RequestOrderResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 발주 품목 교차테이블 Service 구현체
 */
@Service
@RequiredArgsConstructor
public class ItemRoCrossedTableServiceImpl implements ItemRoCrossedTableService {

  private final ItemRoCrossedTableRepository itemRoCrossedTableRepository;
  private final RequestOrderRepository requestOrderRepository;
  private final ItemRepository itemRepository;

  @Override
  @Transactional
  public RequestOrderResponse.RequestOrderLineResponse addLine(final Long roId,
      final RequestOrderRequest.RequestOrderLineRequest request) {
    RequestOrder ro = requestOrderRepository.findById(roId)
        .orElseThrow(() -> new EntityNotFoundException("발주를 찾을 수 없습니다. id=" + roId));

    Item item = itemRepository.findById(request.getItemId())
        .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다. id=" + request.getItemId()));

    ItemRoCrossedTable entity = ItemRoCrossedTable.builder()
        .requestOrder(ro)
        .item(item)
        .qty(request.getQty())
        .build();

    return RequestOrderResponse.RequestOrderLineResponse.from(
        itemRoCrossedTableRepository.save(entity));
  }

  @Override
  @Transactional(readOnly = true)
  public List<RequestOrderResponse.RequestOrderLineResponse> getLinesByRoId(final Long roId) {
    return itemRoCrossedTableRepository.findByRequestOrder_RoId(roId)
        .stream()
        .map(RequestOrderResponse.RequestOrderLineResponse::from)
        .toList();
  }

  @Override
  @Transactional
  public RequestOrderResponse.RequestOrderLineResponse updateLine(final Long roId,
      final Long lineId, final RequestOrderRequest.RequestOrderLineRequest request) {
    ItemRoCrossedTable entity = itemRoCrossedTableRepository.findById(lineId)
        .orElseThrow(() -> new EntityNotFoundException("발주 라인을 찾을 수 없습니다. id=" + lineId));

    if (!entity.getRequestOrder().getRoId().equals(roId)) {
      throw new IllegalArgumentException("해당 발주에 속하지 않는 라인입니다.");
    }

    entity.updateQty(request.getQty());
    return RequestOrderResponse.RequestOrderLineResponse.from(entity);
  }

  @Override
  @Transactional
  public void deleteLine(final Long roId, final Long lineId) {
    ItemRoCrossedTable entity = itemRoCrossedTableRepository.findById(lineId)
        .orElseThrow(() -> new EntityNotFoundException("발주 라인을 찾을 수 없습니다. id=" + lineId));

    if (!entity.getRequestOrder().getRoId().equals(roId)) {
      throw new IllegalArgumentException("해당 발주에 속하지 않는 라인입니다.");
    }

    itemRoCrossedTableRepository.delete(entity);
  }
}
