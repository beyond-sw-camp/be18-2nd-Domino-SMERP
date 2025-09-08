package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.client.Client;
import com.domino.smerp.client.ClientRepository;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemRepository;
import com.domino.smerp.purchase.itemrocrossedtable.ItemRoCrossedTable;
import com.domino.smerp.purchase.itemrocrossedtable.ItemRoCrossedTableRepository;
import com.domino.smerp.purchase.requestorder.constants.RequestOrderStatus;
import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderRequest;
import com.domino.smerp.purchase.requestorder.dto.response.RequestOrderResponse;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrderRepository;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RequestOrderServiceImpl implements RequestOrderService {

  private final RequestOrderRepository requestOrderRepository;
  private final RequestPurchaseOrderRepository requestPurchaseOrderRepository;
  private final ItemRoCrossedTableRepository itemRoCrossedTableRepository;
  private final UserRepository userRepository;
  private final ClientRepository clientRepository;
  private final ItemRepository itemRepository;

  @Override
  @Transactional
  public RequestOrderResponse create(final RequestOrderRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
    Client client = clientRepository.findById(request.getClientId())
        .orElseThrow(() -> new EntityNotFoundException("거래처를 찾을 수 없습니다."));

    RequestPurchaseOrder rpo = null;
    if (request.getRpoId() != null) {
      rpo = requestPurchaseOrderRepository.findById(request.getRpoId())
          .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다."));
    }

    RequestOrder entity = RequestOrder.builder()
        .user(user)
        .client(client)
        .requestPurchaseOrder(rpo)
        .deliveryDate(request.getDeliveryDate())
        .remark(request.getRemark())
        .status(RequestOrderStatus.valueOf(request.getStatus().toUpperCase()))
        .build();

    return RequestOrderResponse.from(requestOrderRepository.save(entity), List.of());
  }

  @Override
  @Transactional(readOnly = true)
  public List<RequestOrderResponse> getAll() {
    return requestOrderRepository.findAll()
        .stream()
        .map(ro -> RequestOrderResponse.from(ro,
            itemRoCrossedTableRepository.findByRequestOrderRoId(ro.getRoId())))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public RequestOrderResponse getById(final Long roId) {
    RequestOrder entity = requestOrderRepository.findById(roId)
        .orElseThrow(() -> new EntityNotFoundException("발주를 찾을 수 없습니다. id=" + roId));

    List<ItemRoCrossedTable> items = itemRoCrossedTableRepository.findByRequestOrderRoId(roId);
    return RequestOrderResponse.from(entity, items);
  }

  @Override
  @Transactional
  public RequestOrderResponse update(final Long roId, final RequestOrderRequest request) {
    RequestOrder entity = requestOrderRepository.findById(roId)
        .orElseThrow(() -> new EntityNotFoundException("발주를 찾을 수 없습니다. id=" + roId));

    entity.updateDeliveryDate(request.getDeliveryDate());
    entity.updateRemark(request.getRemark());
    entity.updateDocumentDate(Instant.now());

    return RequestOrderResponse.from(entity,
        itemRoCrossedTableRepository.findByRequestOrderRoId(roId));
  }

  @Override
  @Transactional
  public RequestOrderResponse updateStatus(final Long roId, final String status) {
    RequestOrder entity = requestOrderRepository.findById(roId)
        .orElseThrow(() -> new EntityNotFoundException("발주를 찾을 수 없습니다. id=" + roId));

    entity.updateStatus(RequestOrderStatus.valueOf(status.toUpperCase()));
    return RequestOrderResponse.from(entity,
        itemRoCrossedTableRepository.findByRequestOrderRoId(roId));
  }

  @Override
  @Transactional
  public void delete(final Long roId) {
    RequestOrder entity = requestOrderRepository.findById(roId)
        .orElseThrow(() -> new EntityNotFoundException("발주를 찾을 수 없습니다. id=" + roId));

    requestOrderRepository.delete(entity);
  }

  // ===== 라인 =====

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
  public List<RequestOrderResponse.RequestOrderLineResponse> getLines(final Long roId) {
    return itemRoCrossedTableRepository.findByRequestOrderRoId(roId)
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
