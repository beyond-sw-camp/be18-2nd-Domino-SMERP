package com.domino.smerp.salesorder.repository;

import com.domino.smerp.salesorder.SalesOrder;
import com.domino.smerp.salesorder.dto.request.SearchExcelSalesOrderRequest;
import com.domino.smerp.salesorder.dto.request.SearchSalesOrderRequest;
import com.domino.smerp.salesorder.dto.response.ExcelSalesOrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SalesOrderQueryRepository {
    Page<SalesOrder> searchSalesOrders(SearchSalesOrderRequest condition, Pageable pageable);

    List<ExcelSalesOrderResponse> searchExcelSalesOrder(SearchExcelSalesOrderRequest condition, Pageable pageable);
}