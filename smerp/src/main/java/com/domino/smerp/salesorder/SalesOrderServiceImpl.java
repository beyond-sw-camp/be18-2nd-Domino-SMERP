package com.domino.smerp.salesorder;

import com.domino.smerp.order.Order;
import com.domino.smerp.order.repository.OrderRepository;
import com.domino.smerp.salesorder.dto.request.SalesOrderRequest;
import com.domino.smerp.salesorder.dto.response.SalesOrderCreateResponse;
import com.domino.smerp.salesorder.dto.response.SalesOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final OrderRepository orderRepository;

    @Override
    public SalesOrderCreateResponse createSalesOrder(SalesOrderRequest request) {
        // 주문 찾아오기
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 ID: " + request.getOrderId()));

        // 판매 전표 생성 (주문만 참조)
        SalesOrder salesOrder = SalesOrder.builder()
                .order(order)                 // ✅ 주문 참조만 세팅
                .salesDate(order.getCreatedAt()) // 주문 생성일 기준으로 판매일자 지정
                .remark(order.getRemark())
                .build();

        return SalesOrderCreateResponse.from(salesOrderRepository.save(salesOrder));
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

        // 여기서는 상태값이나 비고 정도만 수정 가능하게 유지
        salesOrder.updateRemark(request.getRemark());

        return SalesOrderResponse.from(salesOrder);
    }

    @Override
    public void deleteSalesOrder(Long soId) {
        salesOrderRepository.deleteById(soId);
    }
}
