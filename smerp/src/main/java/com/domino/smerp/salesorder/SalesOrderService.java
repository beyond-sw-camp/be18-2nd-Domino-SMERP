package com.domino.smerp.salesorder;

import com.domino.smerp.salesorder.dto.request.SalesOrderRequest;
import com.domino.smerp.salesorder.dto.response.SalesOrderResponse;

import java.util.List;

public interface SalesOrderService {
    SalesOrderResponse createSalesOrder(SalesOrderRequest request);
    List<SalesOrderResponse> getSalesOrders();
    SalesOrderResponse getSalesOrderById(Long soId);
    SalesOrderResponse updateSalesOrder(Long soId, SalesOrderRequest request);
    void deleteSalesOrder(Long soId);
}
