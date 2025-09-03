package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.dto.RequestPurchaseOrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface RequestPurchaseOrderService {

  RequestPurchaseOrder create(RequestPurchaseOrderRequest req);

  RequestPurchaseOrder get(Long id);

  Page<RequestPurchaseOrder> list(Pageable pageable, Long userId,
      RequestPurchaseOrderStatus status);

  RequestPurchaseOrder changeDeliveryDate(Long id, LocalDate deliveryDate);

  RequestPurchaseOrder changeStatus(Long id, RequestPurchaseOrderStatus status, String reason);

  RequestPurchaseOrder changeRemark(Long id, String remark);

  /**
   * 전표일자 변경(PATCH)
   */
  RequestPurchaseOrder changeDocumentDate(Long id, LocalDate date);

  void delete(Long id);
}
