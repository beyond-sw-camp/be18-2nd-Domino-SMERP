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
import com.domino.smerp.order.dto.request.UpdateOrderStatusRequest;
import com.domino.smerp.order.dto.response.OrderResponse;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        // 거래처 확인
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.CLIENT_NOT_FOUND));

        // 사용자 확인
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 🚨 품목 필수 검증
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new CustomException(ErrorCode.ITEMS_REQUIRED);
        }

        // 상태값 기본 처리
        OrderStatus status = (request.getStatus() != null)
                ? request.getStatus()
                : OrderStatus.PENDING;

        // 주문 엔티티 생성
        Order order = Order.builder()
                .client(client)
                .user(user)
                .status(status)
                .deliveryDate(request.getDeliveryDate())
                .remark(request.getRemark())
                .createdDate(LocalDate.now())
                .build();

        // 품목 리스트 매핑 → 교차 테이블 생성
        for (ItemOrderRequest itemReq : request.getItems()) {
            Item item = itemRepository.findById(itemReq.getItemId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

            ItemOrderCrossedTable orderItem = ItemOrderCrossedTable.builder()
                    .order(order)
                    .item(item)
                    .qty(itemReq.getQty())
                    .build();

            order.addOrderItem(orderItem); // 연관관계 편의 메서드
        }

        // CascadeType.ALL 이므로 order만 저장해도 orderItems 함께 저장됨
        return OrderResponse.from(orderRepository.save(order));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(OrderResponse::from)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.updateStatus(request.getStatus());
        return OrderResponse.from(order);
    }

    @Override
    public void deleteOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderRepository.deleteById(orderId);
    }
}
