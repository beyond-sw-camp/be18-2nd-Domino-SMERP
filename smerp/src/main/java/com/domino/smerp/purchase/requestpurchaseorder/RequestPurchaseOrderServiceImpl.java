package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.constants.RequestPurchaseOrderStatus;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.RequestPurchaseOrderRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.response.RequestPurchaseOrderResponse;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestPurchaseOrderServiceImpl implements RequestPurchaseOrderService {

  private final RequestPurchaseOrderRepository requestPurchaseOrderRepository;
  private final UserRepository userRepository;

  @Override
  @Transactional
  public RequestPurchaseOrderResponse create(final RequestPurchaseOrderRequest request) {
    User user = userRepository.findById(request.getUserId())
        .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. id=" + request.getUserId()));

    RequestPurchaseOrder entity = RequestPurchaseOrder.builder()
        .user(user)
        .deliveryAt(request.getDeliveryAt())
        .remark(request.getRemark())
        .status(RequestPurchaseOrderStatus.valueOf(request.getStatus().toUpperCase()))
        .build();

    return RequestPurchaseOrderResponse.from(requestPurchaseOrderRepository.save(entity));
  }

  @Override
  @Transactional(readOnly = true)
  public List<RequestPurchaseOrderResponse> getAll(final String status, final int page, final int size) {
    Pageable pageable = PageRequest.of(page, size);
    List<RequestPurchaseOrder> entities;

    if (status != null && !status.isBlank()) {
      RequestPurchaseOrderStatus orderStatus = RequestPurchaseOrderStatus.valueOf(status.toUpperCase());
      entities = requestPurchaseOrderRepository.findByStatus(orderStatus);
    } else {
      entities = requestPurchaseOrderRepository.findAll(pageable).getContent();
    }

    return entities.stream()
        .map(RequestPurchaseOrderResponse::from)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public RequestPurchaseOrderResponse getById(final Long rpoId) {
    RequestPurchaseOrder entity = requestPurchaseOrderRepository.findById(rpoId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + rpoId));

    return RequestPurchaseOrderResponse.from(entity);
  }

  @Override
  @Transactional
  public RequestPurchaseOrderResponse update(final Long rpoId, final RequestPurchaseOrderRequest request) {
    RequestPurchaseOrder entity = requestPurchaseOrderRepository.findById(rpoId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + rpoId));

    entity.changeDeliveryAt(request.getDeliveryAt());
    entity.changeRemark(request.getRemark());
    entity.changeDocumentDate(Instant.now());

    return RequestPurchaseOrderResponse.from(entity);
  }

  @Override
  @Transactional
  public RequestPurchaseOrderResponse updateStatus(final Long rpoId, final String status, final String reason) {
    RequestPurchaseOrder entity = requestPurchaseOrderRepository.findById(rpoId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + rpoId));

    RequestPurchaseOrderStatus newStatus = RequestPurchaseOrderStatus.valueOf(status.toUpperCase());

    switch (newStatus) {
      case APPROVED -> entity.markApproved();
      case COMPLETED -> entity.markCompleted();
      case RETURNED -> entity.markReturned(reason);
      case PENDING -> entity.revertToPending(reason);
    }

    return RequestPurchaseOrderResponse.from(entity);
  }

  @Override
  @Transactional
  public void softDelete(final Long rpoId) {
    RequestPurchaseOrder entity = requestPurchaseOrderRepository.findById(rpoId)
        .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + rpoId));

    entity.markReturned("소프트 삭제 처리됨");
  }
}
