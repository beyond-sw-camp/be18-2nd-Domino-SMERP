package com.domino.smerp.order;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.order.dto.request.CreateOrderRequest;
import com.domino.smerp.order.dto.request.OrderSearchRequest;
import com.domino.smerp.order.dto.request.UpdateOrderRequest;
import com.domino.smerp.order.dto.response.*;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    CreateOrderResponse createOrder(CreateOrderRequest request);

    PageResponse<ListOrderResponse> getOrders(OrderSearchRequest condition, Pageable pageable);

    DetailOrderResponse getDetailOrder(Long orderId);

    UpdateOrderResponse updateOrder(Long orderId, UpdateOrderRequest request);

    DeleteOrderResponse deleteOrder(Long orderId);
}


