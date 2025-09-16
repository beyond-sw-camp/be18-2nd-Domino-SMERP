package com.domino.smerp.salesorder;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.common.util.DocumentNoGenerator;
import com.domino.smerp.order.Order;
import com.domino.smerp.order.repository.OrderRepository;
import com.domino.smerp.salesorder.constants.SalesOrderStatus;
import com.domino.smerp.salesorder.dto.request.CreateSalesOrderRequest;
import com.domino.smerp.salesorder.dto.request.SearchSalesOrderRequest;
import com.domino.smerp.salesorder.dto.request.UpdateSalesOrderRequest;
import com.domino.smerp.salesorder.dto.response.*;
import com.domino.smerp.salesorder.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final OrderRepository orderRepository;
    private final DocumentNoGenerator documentNoGenerator;

    @Override
    @Transactional
    public CreateSalesOrderResponse createSalesOrder(CreateSalesOrderRequest request) {
        // 주문 전표로 Order 조회
        Order order = orderRepository.findByDocumentNo(request.getOrderDocumentNo())
                .orElseThrow(() -> new CustomException(ErrorCode.SALES_ORDER_NOT_FOUND));

        if (request.getDocumentDate() == null) {
            throw new CustomException(ErrorCode.INVALID_ORDER_REQUEST);
        }

        // 전표번호 생성
        String documentNo = documentNoGenerator.generate(
                request.getDocumentDate(),
                salesOrderRepository::findMaxSequenceByPrefix
        );

        SalesOrder salesOrder = SalesOrder.builder()
                .order(order)
                .documentNo(documentNo)
                .status(SalesOrderStatus.APPROVED)
                .remark(request.getRemark())
                .warehouseName(request.getWarehouseName())
                .build();

        salesOrderRepository.save(salesOrder);
        return CreateSalesOrderResponse.from(salesOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public DetailSalesOrderResponse getDetailSalesOrder(Long salesOrderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        return DetailSalesOrderResponse.from(salesOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ListSalesOrderResponse> getSalesOrders(SearchSalesOrderRequest condition, Pageable pageable) {
        Page<SalesOrder> page = salesOrderRepository.searchSalesOrders(condition, pageable);

        Page<ListSalesOrderResponse> dtoPage = page.map(ListSalesOrderResponse::from);

        return PageResponse.from(dtoPage);
    }

    @Override
    @Transactional
    public UpdateSalesOrderResponse updateSalesOrder(Long salesOrderId, UpdateSalesOrderRequest request) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 전표번호 갱신 로직
        if (request.getDocumentDate() != null) {
            String newDocNo = documentNoGenerator.generateOrKeep(
                    salesOrder.getDocumentNo(),
                    request.getDocumentDate(),
                    salesOrderRepository::findMaxSequenceByPrefix
            );
            salesOrder.updateDocumentInfo(newDocNo);
        }

        salesOrder.updateAll(
                request.getStatus(),
                request.getRemark(),
                request.getWarehouseName()
        );

        return UpdateSalesOrderResponse.from(salesOrderRepository.save(salesOrder));
    }

    @Override
    @Transactional
    public DeleteSalesOrderResponse deleteSalesOrder(Long salesOrderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new CustomException(ErrorCode.SALES_ORDER_NOT_FOUND));

        salesOrderRepository.delete(salesOrder); // 실제 삭제 X → is_deleted = true 업데이트
        return DeleteSalesOrderResponse.from(salesOrder);
    }
}
