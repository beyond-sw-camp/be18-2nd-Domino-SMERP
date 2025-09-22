package com.domino.smerp.salesorder;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.common.util.DocumentNoGenerator;
import com.domino.smerp.order.Order;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.order.repository.OrderRepository;
import com.domino.smerp.salesorder.dto.request.CreateSalesOrderRequest;
import com.domino.smerp.salesorder.dto.request.SearchSalesOrderRequest;
import com.domino.smerp.salesorder.dto.request.SearchSummarySalesOrderRequest;
import com.domino.smerp.salesorder.dto.request.UpdateSalesOrderRequest;
import com.domino.smerp.salesorder.dto.response.*;
import com.domino.smerp.salesorder.repository.SalesOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOrderServiceImpl implements SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final OrderRepository orderRepository;
    private final DocumentNoGenerator documentNoGenerator;

    // 공통 판매 전표일 검증 메서드
    private void validateSalesOrderDate(Order order, LocalDate salesOrderDate) {
        if (salesOrderDate == null) {
            throw new CustomException(ErrorCode.INVALID_ORDER_REQUEST);
        }
        LocalDate orderDate = documentNoGenerator.extractDate(order.getDocumentNo());
        if (salesOrderDate.isBefore(orderDate)) {
            throw new CustomException(ErrorCode.SALES_ORDER_DATE_BEFORE_ORDER_DATE);
        }
    }

    // 판매 등록
    @Override
    @Transactional
    public CreateSalesOrderResponse createSalesOrder(CreateSalesOrderRequest request) {
        // 주문 전표로 Order 조회
        Order order = orderRepository.findByDocumentNo(request.getOrderDocumentNo())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 주문 상태가 APPROVED가 아닌 경우 예외 발생
        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }

        // 판매 전표일 검증 (null + 순서)
        validateSalesOrderDate(order, request.getDocumentDate());

        // 이미 삭제되지 않은 판매가 존재하는 경우 예외 발생
        if (salesOrderRepository.existsByOrderAndIsDeletedFalse(order)) {
            throw new CustomException(ErrorCode.SALES_ORDER_ALREADY_EXISTS);
        }

        // 전표번호 생성
        String documentNo = documentNoGenerator.generate(
                request.getDocumentDate(),
                salesOrderRepository::findMaxSequenceByPrefix
        );

        SalesOrder salesOrder = SalesOrder.builder()
                .order(order)
                .documentNo(documentNo)
                .remark(request.getRemark())
                .warehouseName(request.getWarehouseName())
                .build();

        salesOrderRepository.save(salesOrder);
        return CreateSalesOrderResponse.from(salesOrder);
    }


    // 판매 목록 조회
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ListSalesOrderResponse> getSalesOrders(SearchSalesOrderRequest condition, Pageable pageable) {
        Page<SalesOrder> page = salesOrderRepository.searchSalesOrders(condition, pageable);

        Page<ListSalesOrderResponse> dtoPage = page.map(ListSalesOrderResponse::from);

        return PageResponse.from(dtoPage);
    }

    // 판매 상세 조회
    @Override
    @Transactional(readOnly = true)
    public DetailSalesOrderResponse getDetailSalesOrder(Long salesOrderId) {
        SalesOrder salesOrder = salesOrderRepository.findByIdWithDetails(salesOrderId)
                .orElseThrow(() -> new CustomException(ErrorCode.SALES_ORDER_NOT_FOUND));
        return DetailSalesOrderResponse.from(salesOrder);
    }

    // 판매 수정
    @Override
    @Transactional
    public UpdateSalesOrderResponse updateSalesOrder(Long salesOrderId, UpdateSalesOrderRequest request) {
        SalesOrder salesOrder = salesOrderRepository.findByIdWithDetails(salesOrderId)
                .orElseThrow(() -> new CustomException(ErrorCode.SALES_ORDER_NOT_FOUND));

        if (salesOrder.getOrder().getStatus() == OrderStatus.COMPLETED) {
            throw new CustomException(ErrorCode.SALES_ORDER_ALREADY_COMPLETED);
        }

        if (request.getDocumentDate() != null) {
            // 판매 전표일 검증
            validateSalesOrderDate(salesOrder.getOrder(), request.getDocumentDate());

            String newDocNo = documentNoGenerator.generateOrKeep(
                    salesOrder.getDocumentNo(),
                    request.getDocumentDate(),
                    salesOrderRepository::findMaxSequenceByPrefix
            );
            salesOrder.updateDocumentInfo(newDocNo);
        }


        salesOrder.updateAll(
                request.getRemark(),
                request.getWarehouseName()
        );

        return UpdateSalesOrderResponse.from(salesOrderRepository.save(salesOrder));
    }

    // 판매 삭제
    @Override
    @Transactional
    public DeleteSalesOrderResponse deleteSalesOrder(Long salesOrderId) {
        SalesOrder salesOrder = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new CustomException(ErrorCode.SALES_ORDER_NOT_FOUND));

        if (salesOrder.getOrder().getStatus() == OrderStatus.COMPLETED) {
            throw new CustomException(ErrorCode.SALES_ORDER_ALREADY_COMPLETED);
        }

        salesOrderRepository.delete(salesOrder);
        return DeleteSalesOrderResponse.from(salesOrder);
    }

    // 판매 현황
    @Override
    @Transactional(readOnly = true)
    public List<SummarySalesOrderResponse> getSummarySalesOrder(SearchSummarySalesOrderRequest condition, Pageable pageable) {
        return salesOrderRepository.searchSummarySalesOrder(condition, pageable);
    }

}
