package com.domino.smerp.order;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.order.dto.request.*;
import com.domino.smerp.order.dto.response.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    CreateOrderResponse createOrder(CreateOrderRequest request);

    PageResponse<ListOrderResponse> getOrders(SearchOrderRequest condition, Pageable pageable);

    DetailOrderResponse getDetailOrder(Long orderId);

    UpdateOrderResponse updateOrder(Long orderId, UpdateOrderRequest request);

    DeleteOrderResponse deleteOrder(Long orderId);

    List<ExcelOrderResponse> getExcelOrder(SearchExcelOrderRequest condition, Pageable pageable);

    CreateReturnOrderResponse createReturnOrder(CreateReturnOrderRequest request);

    List<ExcelReturnOrderResponse> getExcelReturnOrders(SearchExcelReturnOrderRequest condition, Pageable pageable);
}


