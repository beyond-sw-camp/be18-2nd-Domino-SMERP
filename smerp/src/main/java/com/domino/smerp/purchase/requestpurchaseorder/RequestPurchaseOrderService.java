package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.RequestPurchaseOrderCreateRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.RequestPurchaseOrderUpdateRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.SearchRequestPurchaseOrderRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.response.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestPurchaseOrderService {

    // ✅ 구매요청 생성
    RequestPurchaseOrderCreateResponse createRequestPurchaseOrder(RequestPurchaseOrderCreateRequest request);

    // ✅ 구매요청 수정
    RequestPurchaseOrderUpdateResponse updateRequestPurchaseOrder(Long rpoId, RequestPurchaseOrderUpdateRequest request);

    PageResponse<RequestPurchaseOrderGetListResponse> searchRequestPurchaseOrders(
            SearchRequestPurchaseOrderRequest keyword, Pageable pageable);

    // ✅ 구매요청 목록 조회
    List<RequestPurchaseOrderGetListResponse> getRequestPurchaseOrders(int page, int size);

    // ✅ 구매요청 상세 조회
    RequestPurchaseOrderGetDetailResponse getRequestPurchaseOrderById(Long rpoId);

    // ✅ 구매요청 삭제 (소프트 삭제)
    RequestPurchaseOrderDeleteResponse deleteRequestPurchaseOrder(Long rpoId);
}
