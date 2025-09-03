package com.domino.smerp.order;

import com.domino.smerp.order.dto.request.OrderRequest;
import com.domino.smerp.order.dto.request.UpdateOrderStatusRequest;
import com.domino.smerp.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    List<OrderResponse> getOrders();
    OrderResponse getOrderById(Long orderId);
    OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);
    void deleteOrder(Long orderId);
}
