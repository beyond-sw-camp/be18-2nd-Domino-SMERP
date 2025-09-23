package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderCreateRequest;
import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderUpdateRequest;
import com.domino.smerp.purchase.requestorder.dto.request.SearchRequestOrderRequest;
import com.domino.smerp.purchase.requestorder.dto.response.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RequestOrderService {

    // ✅ 발주 생성
    RequestOrderCreateResponse createRequestOrder(RequestOrderCreateRequest request);

    // ✅ 발주 수정
    RequestOrderUpdateResponse updateRequestOrder(Long roId, RequestOrderUpdateRequest request);

    PageResponse<RequestOrderGetListResponse> searchRequestOrders(SearchRequestOrderRequest keyword, Pageable pageable);

    // ✅ 발주 목록 조회
    List<RequestOrderGetListResponse> getRequestOrders(int page, int size);

    // ✅ 발주 상세 조회
    RequestOrderGetDetailResponse getRequestOrderById(Long roId);

    // ✅ 발주 삭제
    RequestOrderDeleteResponse deleteRequestOrder(Long roId);
}
