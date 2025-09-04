package com.domino.smerp.salesorder;

import com.domino.smerp.order.Order;
import com.domino.smerp.order.OrderRepository;
import com.domino.smerp.salesorder.constants.SalesOrderStatus;
import com.domino.smerp.salesorder.dto.request.SalesOrderRequest;
import com.domino.smerp.salesorder.dto.response.SalesOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final OrderRepository orderRepository;

    @Override
    public SalesOrderResponse createSalesOrder(SalesOrderRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 ID: " + request.getOrderId()));

        SalesOrder salesOrder = SalesOrder.builder()
                .order(order)
                .qty(request.getQty())
                .surtax(request.getSurtax())
                .price(request.getPrice())
                .remark(request.getRemark())
                .status(SalesOrderStatus.APPROVED)   // ✅ 강제로 APPROVED 지정
                .createdDate(LocalDate.now())
                .build();

        return SalesOrderResponse.from(salesOrderRepository.save(salesOrder));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalesOrderResponse> getSalesOrders() {
        return salesOrderRepository.findAll().stream()
                .map(SalesOrderResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SalesOrderResponse getSalesOrderById(Long soId) {
        return salesOrderRepository.findById(soId)
                .map(SalesOrderResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 판매 ID: " + soId));
    }

    @Override
    public SalesOrderResponse updateSalesOrder(Long soId, SalesOrderRequest request) {
        SalesOrder salesOrder = salesOrderRepository.findById(soId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 판매 ID: " + soId));

        salesOrder.updateStatus(request.getStatus());
        return SalesOrderResponse.from(salesOrder);
    }

    @Override
    public void deleteSalesOrder(Long soId) {
        salesOrderRepository.deleteById(soId);
    }
}
