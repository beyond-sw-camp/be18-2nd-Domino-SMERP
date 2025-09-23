package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.item.dto.request.SearchItemRequest;
import com.domino.smerp.item.dto.response.ItemListResponse;
import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderCreateRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderUpdateRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.SearchPurchaseOrderRequest;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderCreateResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderUpdateResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderGetListResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderGetDetailResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderDeleteResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PurchaseOrderService {

  // ✅ 구매 생성
  PurchaseOrderCreateResponse createPurchaseOrder(final PurchaseOrderCreateRequest request);

  // ✅ 구매 수정
  PurchaseOrderUpdateResponse updatePurchaseOrder(final Long poId, final PurchaseOrderUpdateRequest request);

  PageResponse<PurchaseOrderGetListResponse> searchPurchaseOrdes(final SearchPurchaseOrderRequest keyword,
                                               final Pageable pageable);

  // ✅ 구매 목록 조회 (페이징)
  List<PurchaseOrderGetListResponse> getPurchaseOrders(final int page, final int size);

  // ✅ 구매 상세 조회
  PurchaseOrderGetDetailResponse getPurchaseOrderById(final Long poId);

  // ✅ 구매 삭제 (소프트 삭제)
  PurchaseOrderDeleteResponse deletePurchaseOrder(final Long poId);
}
