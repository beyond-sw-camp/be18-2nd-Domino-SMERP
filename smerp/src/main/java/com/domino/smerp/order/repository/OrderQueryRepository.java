package com.domino.smerp.order.repository;

import com.domino.smerp.order.Order;
import com.domino.smerp.order.dto.request.SearchOrderRequest;
import com.domino.smerp.order.dto.request.SearchSummaryOrderRequest;
import com.domino.smerp.order.dto.request.SearchSummaryReturnOrderRequest;
import com.domino.smerp.order.dto.response.SummaryOrderResponse;
import com.domino.smerp.order.dto.response.SummaryReturnOrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderQueryRepository {
    Page<Order> searchOrders(SearchOrderRequest condition, Pageable pageable);

    List<SummaryOrderResponse> searchSummaryOrders(SearchSummaryOrderRequest condition, Pageable pageable);

    List<SummaryReturnOrderResponse> searchSummaryReturnOrders(SearchSummaryReturnOrderRequest condition, Pageable pageable);
}

