package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemRepository;
import com.domino.smerp.purchase.itemrpocrossedtable.ItemRpoCrossedTable;
import com.domino.smerp.purchase.itemrpocrossedtable.ItemRpoCrossedTableRepository;
import com.domino.smerp.purchase.requestpurchaseorder.constants.RequestPurchaseOrderStatus;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.RequestPurchaseOrderRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.response.RequestPurchaseOrderResponse;
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
public class RequestPurchaseOrderServiceImpl implements RequestPurchaseOrderService {

  private final RequestPurchaseOrderRepository requestPurchaseOrderRepository;
  private final ItemRpoCrossedTableRepository itemRpoCrossedTableRepository;
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;

  // ===== 헤더 =====

  @Override
  @Transactional
  public RequestPurchaseOrderResponse create(final RequestPurchaseOrderRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(
            () -> new EntityNotFoundException("사용자를 찾을 수 없습니다. id=" + request.getUserId()));

    RequestPurchaseOrder entity = RequestPurchaseOrder.builder()
        .user(user)
        .deliveryDate(request.getDeliveryDate())
        .remark(request.getRemark())
        .status(RequestPurchaseOrderStatus.valueOf(request.getStatus().toUpperCase()))
        .documentNo(request.getDocumentNo())
        .build();

    return RequestPurchaseOrderResponse.from(requestPurchaseOrderRepository.save(entity),
        List.of());
  }

  @Override
  @Transactional(readOnly = true)
  public List<RequestPurchaseOrderResponse> getAll() {
    return requestPurchaseOrderRepository.findAll()
        .stream()
        .map(rpo -> RequestPurchaseOrderResponse.from(
            rpo,
            itemRpoCrossedTableRepository.findByRequestPurchaseOrderRpoId(rpo.getRpoId())
        ))
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public RequestPurchaseOrderResponse getById(final Long rpoId) {
    RequestPurchaseOrder entity = requestPurchaseOrderRepository.findById(rpoId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + rpoId));

    List<ItemRpoCrossedTable> items = itemRpoCrossedTableRepository.findByRequestPurchaseOrderRpoId(
        rpoId);
    return RequestPurchaseOrderResponse.from(entity, items);
  }

  @Override
  @Transactional
  public RequestPurchaseOrderResponse update(final Long rpoId,
      final RequestPurchaseOrderRequest request) {
    RequestPurchaseOrder entity = requestPurchaseOrderRepository.findById(rpoId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + rpoId));

    entity.updateDeliveryDate(request.getDeliveryDate());
    entity.updateRemark(request.getRemark());
    entity.updateDocumentDate(Instant.now());

    List<ItemRpoCrossedTable> items = itemRpoCrossedTableRepository.findByRequestPurchaseOrderRpoId(
        rpoId);
    return RequestPurchaseOrderResponse.from(entity, items);
  }

  @Override
  @Transactional
  public RequestPurchaseOrderResponse updateStatus(final Long rpoId, final String status,
      final String reason) {
    RequestPurchaseOrder entity = requestPurchaseOrderRepository.findById(rpoId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + rpoId));

    RequestPurchaseOrderStatus newStatus = RequestPurchaseOrderStatus.valueOf(status.toUpperCase());

    switch (newStatus) {
      case APPROVED -> entity.updateStatusToApproved();
      case COMPLETED -> entity.updateStatusToCompleted();
      case RETURNED -> entity.updateStatusToReturned(reason);
      case PENDING -> entity.updateStatusToPending(reason);
    }

    List<ItemRpoCrossedTable> items = itemRpoCrossedTableRepository.findByRequestPurchaseOrderRpoId(
        rpoId);
    return RequestPurchaseOrderResponse.from(entity, items);
  }

  @Override
  @Transactional
  public void softDelete(final Long rpoId) {
    RequestPurchaseOrder entity = requestPurchaseOrderRepository.findById(rpoId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + rpoId));

    entity.markAsDeleted();
  }

  // ===== 라인 =====

  @Override
  @Transactional
  public RequestPurchaseOrderResponse.RequestPurchaseOrderLineResponse addLine(
      final Long rpoId,
      final RequestPurchaseOrderRequest.RequestPurchaseOrderLineRequest request) {

    RequestPurchaseOrder rpo = requestPurchaseOrderRepository.findById(rpoId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + rpoId));

    Item item = itemRepository.findById(request.getItemId())
        .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다. id=" + request.getItemId()));

    ItemRpoCrossedTable entity = ItemRpoCrossedTable.builder()
        .requestPurchaseOrder(rpo)
        .item(item)
        .qty(request.getQty())
        .build();

    return RequestPurchaseOrderResponse.RequestPurchaseOrderLineResponse.from(
        itemRpoCrossedTableRepository.save(entity));
  }

  @Override
  @Transactional(readOnly = true)
  public List<RequestPurchaseOrderResponse.RequestPurchaseOrderLineResponse> getLinesByRpoId(
      final Long rpoId) {
    return itemRpoCrossedTableRepository.findByRequestPurchaseOrderRpoId(rpoId)
        .stream()
        .map(RequestPurchaseOrderResponse.RequestPurchaseOrderLineResponse::from)
        .toList();
  }

  @Override
  @Transactional
  public RequestPurchaseOrderResponse.RequestPurchaseOrderLineResponse updateLine(
      final Long rpoId,
      final Long lineId,
      final RequestPurchaseOrderRequest.RequestPurchaseOrderLineRequest request) {

    ItemRpoCrossedTable entity = itemRpoCrossedTableRepository.findById(lineId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청 라인을 찾을 수 없습니다. id=" + lineId));

    if (!entity.getRequestPurchaseOrder().getRpoId().equals(rpoId)) {
      throw new IllegalArgumentException("해당 구매요청에 속하지 않는 라인입니다.");
    }

    entity.updateQty(request.getQty());
    return RequestPurchaseOrderResponse.RequestPurchaseOrderLineResponse.from(entity);
  }

  @Override
  @Transactional
  public void deleteLine(final Long rpoId, final Long lineId) {
    ItemRpoCrossedTable entity = itemRpoCrossedTableRepository.findById(lineId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청 라인을 찾을 수 없습니다. id=" + lineId));

    if (!entity.getRequestPurchaseOrder().getRpoId().equals(rpoId)) {
      throw new IllegalArgumentException("해당 구매요청에 속하지 않는 라인입니다.");
    }

    itemRpoCrossedTableRepository.delete(entity);
  }
}
