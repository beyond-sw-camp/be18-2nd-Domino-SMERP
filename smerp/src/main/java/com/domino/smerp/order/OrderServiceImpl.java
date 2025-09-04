package com.domino.smerp.order;

import com.domino.smerp.client.Client;
import com.domino.smerp.client.ClientRepository;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
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

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.CLIENT_NOT_FOUND));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

//        // ✅ status 검증
//        OrderStatus status;
//        try {
//            status = (request.getStatus() != null) ? request.getStatus() : OrderStatus.PENDING;
//        } catch (IllegalArgumentException e) {
//            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
//        }

        OrderStatus status = (request.getStatus() != null)
                ? request.getStatus()
                : OrderStatus.PENDING;

        OrderStatus status = (request.getStatus() != null)
                ? request.getStatus()
                : OrderStatus.PENDING;

        Order order = Order.builder()
                .client(client)
                .user(user)
                .status(status)   // default값 입력
                .deliveryDate(request.getDeliveryDate())
                .remark(request.getRemark())
                .createdDate(LocalDate.now())
                .build();

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

//        // ✅ 상태값 검증
//        try {
//            order.updateStatus(request.getStatus());
//        } catch (IllegalArgumentException e) {
//            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
//        }

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
