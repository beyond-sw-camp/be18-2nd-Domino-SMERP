package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.dto.RequestPurchaseOrderRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestPurchaseOrderServiceImpl implements RequestPurchaseOrderService {

  private final RequestPurchaseOrderRepository repository;

  @Override
  public RequestPurchaseOrder create(RequestPurchaseOrderRequest req) {
    RequestPurchaseOrder entity = RequestPurchaseOrder.builder()
        .userId(req.userId())
        .deliveryDate(req.deliveryDate())   // null → @PrePersist가 today
        .remark(req.remark())
        .status(req.status())               // null → @PrePersist가 PENDING
        .updatedDate(req.updatedDate())     // null → @PrePersist가 created_date
        .build();
    return repository.save(entity);
  }

  @Transactional(readOnly = true)
  @Override
  public RequestPurchaseOrder get(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("RPO not found: " + id));
  }

  @Transactional(readOnly = true)
  @Override
  public Page<RequestPurchaseOrder> list(Pageable pageable, Long userId,
      RequestPurchaseOrderStatus status) {
      if (userId != null && status != null) {
          return repository.findByUserIdAndStatus(userId, status, pageable);
      }
      if (userId != null) {
          return repository.findByUserId(userId, pageable);
      }
      if (status != null) {
          return repository.findByStatus(status, pageable);
      }
    return repository.findAll(pageable);
  }

  @Override
  public RequestPurchaseOrder changeDeliveryDate(Long id, LocalDate deliveryDate) {
    RequestPurchaseOrder e = get(id);
    e.changeDeliveryDate(deliveryDate);
    return e;
  }

  @Override
  public RequestPurchaseOrder changeStatus(Long id, RequestPurchaseOrderStatus status,
      String reason) {
    RequestPurchaseOrder e = get(id);
    switch (status) {
      case APPROVED -> e.markApproved();
      case COMPLETED -> e.markCompleted();
      case RETURNED -> e.markReturned(reason);
      case PENDING -> e.revertToPending(reason);
    }
    return e;
  }

  @Override
  public RequestPurchaseOrder changeRemark(Long id, String remark) {
    RequestPurchaseOrder e = get(id);
    e.changeRemark(remark);
    return e;
  }

  @Override
  public RequestPurchaseOrder changeDocumentDate(Long id, LocalDate date) {
    RequestPurchaseOrder e = get(id);
    e.changeDocumentDate(date);
    return e;
  }

  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }
}
