package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.client.Client;
import com.domino.smerp.client.ClientRepository;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RequestOrderServiceImpl implements
    com.domino.smerp.purchase.requestorder.ItemRoCrossedTableService {

  private final RequestOrderRepository requestOrderRepository;
  private final RequestPurchaseOrderRepository requestPurchaseOrderRepository;
  private final UserRepository userRepository;
  private final ClientRepository clientRepository;
  private final ItemRepository itemRepository;

  @Override
  @Transactional
  public RequestOrderResponse create(final RequestOrderRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(
            () -> new EntityNotFoundException("사용자를 찾을 수 없습니다. id=" + request.getUserId()));

    Client client = clientRepository.findById(request.getClientId())
        .orElseThrow(
            () -> new EntityNotFoundException("거래처를 찾을 수 없습니다. id=" + request.getClientId()));

    RequestPurchaseOrder rpo = null;
    if (request.getRpoId() != null) {
      rpo = requestPurchaseOrderRepository.findById(request.getRpoId())
          .orElseThrow(
              () -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + request.getRpoId()));
      // 구매요청 자동 승인 처리
      rpo.markApproved();
    }

    RequestOrder entity = RequestOrder.builder()
        .user(user)
        .client(client)
        .requestPurchaseOrder(rpo)
        .deliveryAt(request.getDeliveryAt())
        .remark(request.getRemark())
        .status(RequestOrderStatus.valueOf(request.getStatus().toUpperCase()))
        .build();

    // 품목 라인 추가
    request.getItems().forEach(line -> {
      Item item = itemRepository.findById(line.getItemId())
          .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다. id=" + line.getItemId()));

      ItemRoCrossedTable itemLine = ItemRoCrossedTable.builder()
          .requestOrder(entity)
          .item(item)
          .qty(line.getQty())
          .build();

      entity.addItem(itemLine);
    });

    return RequestOrderResponse.from(requestOrderRepository.save(entity));
  }

  @Override
  @Transactional(readOnly = true)
  public List<RequestOrderResponse> getAll(final String status, final int page, final int size) {
    Pageable pageable = PageRequest.of(page, size);
    List<RequestOrder> entities;

    if (status != null && !status.isBlank()) {
      RequestOrderStatus orderStatus = RequestOrderStatus.valueOf(status.toUpperCase());
      entities = requestOrderRepository.findByStatus(orderStatus);
    } else {
      entities = requestOrderRepository.findAll(pageable).getContent();
    }

    return entities.stream()
        .map(RequestOrderResponse::from)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public RequestOrderResponse getById(final Long roId) {
    RequestOrder entity = requestOrderRepository.findById(roId)
        .orElseThrow(() -> new EntityNotFoundException("발주를 찾을 수 없습니다. id=" + roId));

    return RequestOrderResponse.from(entity);
  }

  @Override
  @Transactional
  public RequestOrderResponse update(final Long roId, final RequestOrderRequest request) {
    RequestOrder entity = requestOrderRepository.findById(roId)
        .orElseThrow(() -> new EntityNotFoundException("발주를 찾을 수 없습니다. id=" + roId));

    entity.changeDeliveryAt(request.getDeliveryAt());
    entity.changeRemark(request.getRemark());
    entity.changeDocumentDate(Instant.now());

    return RequestOrderResponse.from(entity);
  }

  @Override
  @Transactional
  public RequestOrderResponse updateStatus(final Long roId, final String status,
      final String reason) {
    RequestOrder entity = requestOrderRepository.findById(roId)
        .orElseThrow(() -> new EntityNotFoundException("발주를 찾을 수 없습니다. id=" + roId));

    RequestOrderStatus newStatus = RequestOrderStatus.valueOf(status.toUpperCase());

    switch (newStatus) {
      case APPROVED -> entity.markApproved();
      case COMPLETED -> entity.markCompleted();
      case RETURNED -> entity.markReturned(reason);
      case PENDING -> entity.revertToPending(reason);
    }

    return RequestOrderResponse.from(entity);
  }

  @Override
  @Transactional
  public void softDelete(final Long roId) {
    RequestOrder entity = requestOrderRepository.findById(roId)
        .orElseThrow(() -> new EntityNotFoundException("발주를 찾을 수 없습니다. id=" + roId));

    entity.markReturned("소프트 삭제 처리됨");
  }
}
