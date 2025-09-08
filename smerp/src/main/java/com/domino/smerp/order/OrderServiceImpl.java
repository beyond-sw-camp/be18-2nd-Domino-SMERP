package com.domino.smerp.order;

import com.domino.smerp.client.Client;
import com.domino.smerp.client.ClientRepository;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemRepository;
import com.domino.smerp.itemorder.ItemOrderCrossedTable;
import com.domino.smerp.itemorder.dto.request.ItemOrderRequest;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.order.dto.request.OrderRequest;
import com.domino.smerp.order.dto.request.UpdateOrderRequest;
import com.domino.smerp.order.dto.response.OrderCreateResponse;
import com.domino.smerp.order.dto.response.OrderDeleteResponse;
import com.domino.smerp.order.dto.response.OrderResponse;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /**
     * 전표번호 생성 (yyyy/MM/dd-순번)
     */
    private String generateDocumentNo(Instant baseInstant) {
        LocalDate baseDate = baseInstant.atZone(ZoneOffset.UTC).toLocalDate(); // ✅ UTC 기준
        String datePart = baseDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        long count = orderRepository.countByDocumentNoStartingWith(datePart);
        return datePart + "-" + (count + 1);
    }

    /**
     * 주문 등록
     */
    @Override
    @Transactional
    public OrderCreateResponse createOrder(OrderRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.CLIENT_NOT_FOUND));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new CustomException(ErrorCode.ITEMS_REQUIRED);
        }

        OrderStatus status = (request.getStatus() != null) ? request.getStatus() : OrderStatus.PENDING;

        // ✅ LocalDate → Instant (UTC 변환)
        Instant orderDateInstant = request.getOrderDate() != null
                ? request.getOrderDate().atStartOfDay(ZoneOffset.UTC).toInstant()
                : null;

        Instant deliveryDateInstant = request.getDeliveryDate() != null
                ? request.getDeliveryDate().atStartOfDay(ZoneOffset.UTC).toInstant()
                : null;

        Order order = Order.builder()
                .client(client)
                .user(user)
                .status(status)
                .deliveryDate(deliveryDateInstant)
                .remark(request.getRemark())
                .documentNo("TEMP") // 이후 updateDocumentInfo로 교체
                .build();

        // ✅ updatedAt = orderDate or null, documentNo 생성
        Instant baseInstant = (orderDateInstant != null) ? orderDateInstant : Instant.now();
        String documentNo = generateDocumentNo(baseInstant);
        order.updateDocumentInfo(orderDateInstant, documentNo);

        for (ItemOrderRequest itemReq : request.getItems()) {
            Item item = itemRepository.findById(itemReq.getItemId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

            ItemOrderCrossedTable orderItem = ItemOrderCrossedTable.builder()
                    .order(order)
                    .item(item)
                    .qty(itemReq.getQty())
                    .build();

            order.addOrderItem(orderItem);
        }

        return OrderCreateResponse.from(orderRepository.save(order));
    }

    /**
     * 주문 목록 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }

    /**
     * 주문 단건 조회
     */
    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(OrderResponse::from)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    /**
     * 주문 전체 수정 (PUT)
     */
    @Override
    @Transactional
    public OrderResponse updateOrder(Long orderId, UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        User newUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new CustomException(ErrorCode.ITEMS_REQUIRED);
        }

        List<ItemOrderCrossedTable> newOrderItems = request.getItems().stream()
                .map(itemReq -> {
                    Item item = itemRepository.findById(itemReq.getItemId())
                            .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

                    return ItemOrderCrossedTable.builder()
                            .order(order)
                            .item(item)
                            .qty(itemReq.getQty())
                            .build();
                })
                .toList();

        // ✅ LocalDate → Instant (UTC 변환)
        Instant newOrderDateInstant = request.getOrderDate() != null
                ? request.getOrderDate().atStartOfDay(ZoneOffset.UTC).toInstant()
                : null;

        Instant newDeliveryDateInstant = request.getDeliveryDate() != null
                ? request.getDeliveryDate().atStartOfDay(ZoneOffset.UTC).toInstant()
                : null;

        // ✅ 전표번호 재생성
        Instant baseInstant = (newOrderDateInstant != null) ? newOrderDateInstant : order.getCreatedAt();
        String newDocumentNo = generateDocumentNo(baseInstant);
        order.updateDocumentInfo(newOrderDateInstant, newDocumentNo);

        order.updateAll(
                newOrderDateInstant,
                newDeliveryDateInstant,
                request.getRemark(),
                request.getStatus(),
                newUser,
                newOrderItems
        );

        return OrderResponse.from(orderRepository.save(order));
    }

    /**
     * 주문 삭제
     */
    @Override
    @Transactional
    public OrderDeleteResponse deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        orderRepository.delete(order); // @SQLDelete soft delete

        return OrderDeleteResponse.from(order);
    }
}
