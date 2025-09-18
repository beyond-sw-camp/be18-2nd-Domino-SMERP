package com.domino.smerp.order;

import com.domino.smerp.client.Client;
import com.domino.smerp.client.ClientRepository;
import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.common.util.DocumentNoGenerator;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemServiceImpl;
import com.domino.smerp.itemorder.ItemOrder;
import com.domino.smerp.itemorder.ItemOrderRepository;
import com.domino.smerp.itemorder.dto.request.ItemOrderRequest;
import com.domino.smerp.itemorder.dto.request.UpdateItemOrderRequest;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.order.dto.request.CreateOrderRequest;
import com.domino.smerp.order.dto.request.SearchExcelOrderRequest;
import com.domino.smerp.order.dto.request.SearchOrderRequest;
import com.domino.smerp.order.dto.request.UpdateOrderRequest;
import com.domino.smerp.order.dto.response.*;
import com.domino.smerp.order.repository.OrderRepository;
import com.domino.smerp.salesorder.repository.SalesOrderRepository;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ItemServiceImpl itemServiceImpl;
    private final ItemOrderRepository itemOrderRepository;
    private final DocumentNoGenerator documentNoGenerator;
    private final SalesOrderRepository salesOrderRepository;

    // 공용 조회용 메서드
    public Order findOrderById(Long orderId) {
        return orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    private Client getClientByCompanyName(String companyName) {
        return clientRepository.findByCompanyName(companyName)
                .orElseThrow(() -> new CustomException(ErrorCode.CLIENT_NOT_FOUND));
    }

    private User getUserByEmpNo(String empNo) {
        return userRepository.findByEmpNo(empNo)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // 전표 생성
    @Transactional
    public String generateDocumentNoWithRetry(LocalDate documentDate) {
        return documentNoGenerator.generate(documentDate, orderRepository::findMaxSequenceByPrefix);
    }

    // 등록용 ItemOrder 생성 메서드
    private ItemOrder toOrderItem(Order order, ItemOrderRequest itemReq) {
        Item item = itemServiceImpl.findItemById(itemReq.getItemId());

        BigDecimal specialPrice = itemReq.getSpecialPrice() != null
                ? itemReq.getSpecialPrice()
                : item.getOutboundUnitPrice();

        return ItemOrder.builder()
                .order(order)
                .item(item)
                .qty(itemReq.getQty())
                .specialPrice(specialPrice)
                .build();
    }

    // 주문 등록
    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        Client client = getClientByCompanyName(request.getCompanyName());
        User user = getUserByEmpNo(request.getEmpNo());

        if (request.getDocumentDate() == null) {
            throw new CustomException(ErrorCode.INVALID_ORDER_REQUEST);
        }

        String documentNo = generateDocumentNoWithRetry(request.getDocumentDate());

        Instant deliveryDateInstant = request.getDeliveryDate() != null
                ? request.getDeliveryDate().atStartOfDay(ZoneOffset.UTC).toInstant()
                : null;

        Order order = Order.builder()
                .client(client)
                .user(user)
                .status(OrderStatus.PENDING)
                .deliveryDate(deliveryDateInstant)
                .remark(request.getRemark())
                .documentNo(documentNo)
                .build();

        request.getItems().forEach(itemReq -> order.addOrderItem(toOrderItem(order, itemReq)));

        orderRepository.save(order);
        itemOrderRepository.saveAll(order.getOrderItems());

        return CreateOrderResponse.from(order);
    }

    // 주문 목록 조회
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ListOrderResponse> getOrders(SearchOrderRequest condition, Pageable pageable) {
        Page<Order> page = orderRepository.searchOrders(condition, pageable);
        return PageResponse.from(page.map(ListOrderResponse::from));
    }

    // 주문 상세 조회
    @Override
    @Transactional(readOnly = true)
    public DetailOrderResponse getDetailOrder(Long orderId) {
        return DetailOrderResponse.from(findOrderById(orderId));
    }

    // 주문 수정
    @Override
    @Transactional
    public UpdateOrderResponse updateOrder(Long orderId, UpdateOrderRequest request) {
        Order order = findOrderById(orderId);
        User user = getUserByEmpNo(request.getEmpNo());

        Map<Long, ItemOrder> existingItems = order.getOrderItems().stream()
                .collect(Collectors.toMap(ItemOrder::getItemOrderId, io -> io));

        List<ItemOrder> finalItems = new ArrayList<>();

        for (UpdateItemOrderRequest itemReq : request.getItems()) {
            if (itemReq.getItemOrderId() != null) {
                ItemOrder existing = existingItems.get(itemReq.getItemOrderId());
                if (existing == null) throw new CustomException(ErrorCode.ITEM_NOT_FOUND);

                existing.updateQty(itemReq.getQty());
                existing.updateSpecialPrice(itemReq.getSpecialPrice());
                finalItems.add(existing);
                existingItems.remove(itemReq.getItemOrderId());
            } else {
                Item item = itemServiceImpl.findItemById(itemReq.getItemId());
                ItemOrder newItem = ItemOrder.builder()
                        .order(order)
                        .item(item)
                        .qty(itemReq.getQty())
                        .specialPrice(itemReq.getSpecialPrice() != null
                                ? itemReq.getSpecialPrice()
                                : item.getOutboundUnitPrice())
                        .build();
                itemOrderRepository.save(newItem);
                order.addOrderItem(newItem);
                finalItems.add(newItem);
            }
        }

        for (ItemOrder toDelete : existingItems.values()) {
            itemOrderRepository.delete(toDelete);
            order.getOrderItems().remove(toDelete);
        }

        if (request.getDocumentDate() != null) {
            String newDocNo = documentNoGenerator.generateOrKeep(
                    order.getDocumentNo(),
                    request.getDocumentDate(),
                    orderRepository::findMaxSequenceByPrefix
            );
            order.updateDocumentInfo(newDocNo);
        }

        Instant newDeliveryDate = request.getDeliveryDate() != null
                ? request.getDeliveryDate().atStartOfDay(ZoneOffset.UTC).toInstant()
                : null;

        order.updateAll(newDeliveryDate, request.getRemark(), request.getStatus(), user, finalItems);

        return UpdateOrderResponse.from(orderRepository.save(order));
    }

    // 주문 삭제
    @Override
    @Transactional
    public DeleteOrderResponse deleteOrder(Long orderId) {
        Order order = findOrderById(orderId);

        // 주문 상태가 APPROVED인 경우 삭제 불가
        if (order.getStatus() == OrderStatus.APPROVED) {
            throw new CustomException(ErrorCode.ORDER_ALREADY_APPROVED);
        }

        // 판매가 없을 때만 삭제 가능
        orderRepository.delete(order);
        return DeleteOrderResponse.from(order);
    }

    // 주문 현황
    @Override
    @Transactional(readOnly = true)
    public List<ExcelOrderResponse> getExcelOrder(SearchExcelOrderRequest condition, Pageable pageable) {
        return orderRepository.searchExcelOrders(condition, pageable);
    }
}
