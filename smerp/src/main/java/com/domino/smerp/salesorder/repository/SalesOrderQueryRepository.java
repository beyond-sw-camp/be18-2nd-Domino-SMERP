package com.domino.smerp.salesorder.repository;

import com.domino.smerp.salesorder.SalesOrder;
import com.domino.smerp.salesorder.dto.request.SearchSalesOrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalesOrderQueryRepository {
    Page<SalesOrder> searchSalesOrders(SearchSalesOrderRequest condition, Pageable pageable);
}