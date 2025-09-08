package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderRequest;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderResponse;
import com.domino.smerp.purchase.requestorder.RequestOrder;
import com.domino.smerp.purchase.requestorder.RequestOrderRepository;
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
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

  private final PurchaseOrderRepository purchaseOrderRepository;
  private final RequestOrderRepository requestOrderRepository;

  @Override
  @Transactional
  public PurchaseOrderResponse create(final PurchaseOrderRequest request) {
    if (request.getQty() == 0) {
      throw new IllegalArgumentException("구매 수량(qty)은 0일 수 없습니다.");
    }

    RequestOrder ro = requestOrderRepository.findById(request.getRoId())
        .orElseThrow(
            () -> new EntityNotFoundException("발주 전표를 찾을 수 없습니다. id=" + request.getRoId()));

    PurchaseOrder entity = PurchaseOrder.builder()
        .requestOrder(ro)
        .qty(request.getQty())
        .surtax(request.getSurtax())
        .price(request.getPrice())
        .remark(request.getRemark())
        .build();

    return PurchaseOrderResponse.from(purchaseOrderRepository.save(entity));
  }

  @Override
  @Transactional(readOnly = true)
  public List<PurchaseOrderResponse> getAll(final int page, final int size) {
    Pageable pageable = PageRequest.of(page, size);
    return purchaseOrderRepository.findAll(pageable).stream()
        .map(PurchaseOrderResponse::from)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public PurchaseOrderResponse getById(final Long poId) {
    PurchaseOrder entity = purchaseOrderRepository.findById(poId)
        .orElseThrow(() -> new EntityNotFoundException("구매 전표를 찾을 수 없습니다. id=" + poId));

    return PurchaseOrderResponse.from(entity);
  }

  @Override
  @Transactional
  public PurchaseOrderResponse update(final Long poId, final PurchaseOrderRequest request) {
    if (request.getQty() == 0) {
      throw new IllegalArgumentException("구매 수량(qty)은 0일 수 없습니다.");
    }

    PurchaseOrder entity = purchaseOrderRepository.findById(poId)
        .orElseThrow(() -> new EntityNotFoundException("구매 전표를 찾을 수 없습니다. id=" + poId));

    entity.updateQty(request.getQty());
    entity.updateSurtax(request.getSurtax());
    entity.updatePrice(request.getPrice());
    entity.updateRemark(request.getRemark());
    entity.updateDocumentDate(Instant.now());

    return PurchaseOrderResponse.from(entity);
  }

  @Override
  @Transactional
  public void delete(final Long poId) {
    PurchaseOrder entity = purchaseOrderRepository.findById(poId)
        .orElseThrow(() -> new EntityNotFoundException("구매 전표를 찾을 수 없습니다. id=" + poId));
    purchaseOrderRepository.delete(entity);
  }
}
