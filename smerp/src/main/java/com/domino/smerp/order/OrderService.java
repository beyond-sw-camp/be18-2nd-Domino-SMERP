package com.domino.smerp.order;

import com.domino.smerp.order.dto.request.CreateOrderRequest;
import com.domino.smerp.order.dto.request.UpdateOrderRequest;
import com.domino.smerp.order.dto.response.*;

import java.util.List;

public interface OrderService {
    CreateOrderResponse createOrder(CreateOrderRequest request);

    List<ListOrderResponse> getOrders();

    DetailOrderResponse getDetailOrder(Long orderId);

    UpdateOrderResponse updateOrder(Long orderId, UpdateOrderRequest request);

    DeleteOrderResponse deleteOrder(Long orderId);
}

