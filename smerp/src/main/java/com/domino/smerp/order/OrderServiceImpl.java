package com.domino.smerp.order;

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
import com.domino.smerp.order.dto.response.OrderResponse;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문 등록
     */
    @Override
    public OrderCreateResponse createOrder(OrderRequest request) {
        var client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.CLIENT_NOT_FOUND));
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new CustomException(ErrorCode.ITEMS_REQUIRED);
        }

        var status = (request.getStatus() != null) ? request.getStatus() : OrderStatus.PENDING;

        var order = Order.builder()
                .client(client)
                .user(user)
                .status(status)
                .orderDate(
                        request.getOrderDate()
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                )
                .deliveryDate(
                        request.getDeliveryDate()
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                )
                .remark(request.getRemark())
                .createdDate(Instant.now())
                .build();

        for (ItemOrderRequest itemReq : request.getItems()) {
            Item item = itemRepository.findById(itemReq.getItemId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

            var orderItem = ItemOrderCrossedTable.builder()
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
    public OrderResponse updateOrder(Long orderId, UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        User newUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new CustomException(ErrorCode.ITEMS_REQUIRED);
        }

        // 새로운 orderItems 생성
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

        // 엔티티에 전체 업데이트 적용
        order.updateAll(
                request.getOrderDate()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant(),
                request.getDeliveryDate()
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant(),
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
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderRepository.deleteById(orderId);
    }
}
