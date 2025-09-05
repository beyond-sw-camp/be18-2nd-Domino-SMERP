package com.domino.smerp.order;

import com.domino.smerp.order.dto.request.OrderRequest;
import com.domino.smerp.order.dto.request.UpdateOrderRequest;
import com.domino.smerp.order.dto.response.OrderCreateResponse;
import com.domino.smerp.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderCreateResponse createOrder(OrderRequest request);
    List<OrderResponse> getOrders();
    OrderResponse getOrderById(Long orderId);
    OrderResponse updateOrderStatus(Long orderId, UpdateOrderRequest request);
    void deleteOrder(Long orderId);
}
