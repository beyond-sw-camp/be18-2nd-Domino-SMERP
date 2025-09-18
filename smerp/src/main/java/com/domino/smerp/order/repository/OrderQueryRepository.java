package com.domino.smerp.order.repository;

import com.domino.smerp.order.Order;
import com.domino.smerp.order.dto.request.SearchExcelOrderRequest;
import com.domino.smerp.order.dto.request.SearchOrderRequest;
import com.domino.smerp.order.dto.response.ExcelOrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderQueryRepository {
    Page<Order> searchOrders(SearchOrderRequest condition, Pageable pageable);

    List<ExcelOrderResponse> searchExcelOrders(SearchExcelOrderRequest condition, Pageable pageable);
}
