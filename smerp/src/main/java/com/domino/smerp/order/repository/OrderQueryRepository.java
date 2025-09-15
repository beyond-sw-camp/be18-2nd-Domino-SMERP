package com.domino.smerp.order.repository;

import com.domino.smerp.order.Order;
import com.domino.smerp.order.dto.request.OrderSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryRepository {
    Page<Order> searchOrders(OrderSearchRequest condition, Pageable pageable);
}
