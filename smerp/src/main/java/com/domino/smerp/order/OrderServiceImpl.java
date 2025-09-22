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
import com.domino.smerp.order.dto.request.*;
import com.domino.smerp.order.dto.response.*;
import com.domino.smerp.order.repository.OrderRepository;
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
import java.util.*;
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

    // 수량 검증 메서드
    private void validateQty(BigDecimal qty) {
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException(ErrorCode.INVALID_QUANTITY);
        }
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

        request.getItems().forEach(itemReq -> {
            validateQty(itemReq.getQty());                  // 검증
            ItemOrder itemOrder = toOrderItem(order, itemReq); // 객체 생성
            order.addOrderItem(itemOrder);                  // 연관관계 세팅
        });

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
                if (existing == null) throw new CustomException(ErrorCode.RETURN_ITEM_NOT_FOUND_IN_ORDER);

                validateQty(itemReq.getQty()); // 수량 검증
                existing.updateQty(itemReq.getQty());
                existing.updateSpecialPrice(itemReq.getSpecialPrice());
                finalItems.add(existing);
                existingItems.remove(itemReq.getItemOrderId());
            } else {
                validateQty(itemReq.getQty()); // 수량 검증
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
        Order order = orderRepository.findByIdForDelete(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

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
    public List<SummaryOrderResponse> getSummaryOrder(SearchSummaryOrderRequest condition, Pageable pageable) {
        return orderRepository.searchSummaryOrders(condition, pageable);
    }

    // 반품 등록
    @Override
    @Transactional
    public CreateReturnOrderResponse createReturnOrder(CreateReturnOrderRequest request) {
        // 원 주문 조회
        Order originalOrder = orderRepository.findByDocumentNo(request.getDocumentNo())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 반품 가능 상태 검증
        if (originalOrder.getStatus() != OrderStatus.COMPLETED) {
            throw new CustomException(ErrorCode.RETURN_ONLY_ALLOWED_AFTER_COMPLETED);
        }

        User user = getUserByEmpNo(request.getEmpNo());

        // 기존 반품 전표들 조회
        List<Order> existingReturns =
                orderRepository.findByDocumentNoStartingWith(originalOrder.getDocumentNo() + "(-");

        // 반품 전표번호 생성 (유틸 사용)
        String documentNo = documentNoGenerator.generateReturnDocumentNo(
                originalOrder.getDocumentNo(),
                existingReturns.stream().map(Order::getDocumentNo).toList()
        );

        Order returnOrder = Order.builder()
                .client(originalOrder.getClient())
                .user(user)
                .status(OrderStatus.COMPLETED)  // 반품은 상태 고정
                .remark(request.getRemark())
                .documentNo(documentNo)
                .deliveryDate(originalOrder.getDeliveryDate()) // delivery_at은 null 허용 안되니 원 주문값 복사
                .build();

        // 원 주문 품목 매핑
        Map<Long, ItemOrder> originalItemMap = originalOrder.getOrderItems().stream()
                .collect(Collectors.toMap(io -> io.getItem().getItemId(), io -> io));

        // 기존 반품 수량 누적
        Map<Long, BigDecimal> alreadyReturnedQty = new HashMap<>();
        existingReturns.forEach(r -> r.getOrderItems().forEach(returnItem -> alreadyReturnedQty.merge(
                returnItem.getItem().getItemId(),
                returnItem.getQty().abs(),   // qty는 음수로 저장했으니 절댓값 처리
                BigDecimal::add
        )));

        // 요청 품목 검증 및 반품 품목 생성
        request.getItems().forEach(itemReq -> {
            ItemOrder originalItem = originalItemMap.get(itemReq.getItemId());
            if (originalItem == null) {
                throw new CustomException(ErrorCode.RETURN_ITEM_NOT_FOUND_IN_ORDER);
            }

            // 누적 반품 수량 검증
            BigDecimal originalQty = originalItem.getQty();
            BigDecimal alreadyReturned = alreadyReturnedQty.getOrDefault(itemReq.getItemId(), BigDecimal.ZERO);
            BigDecimal newReturnTotal = alreadyReturned.add(itemReq.getQty());

            if (newReturnTotal.compareTo(originalQty) > 0) {
                throw new CustomException(ErrorCode.RETURN_QTY_EXCEEDS_ORIGINAL);
            }

            // 반품은 음수 저장
            BigDecimal qty = itemReq.getQty().negate();

            ItemOrder returnItem = ItemOrder.builder()
                    .order(returnOrder)
                    .item(originalItem.getItem())
                    .qty(qty)
                    .specialPrice(originalItem.getSpecialPrice())
                    .build();

            returnOrder.addOrderItem(returnItem);
        });

        // 저장
        orderRepository.save(returnOrder);
        itemOrderRepository.saveAll(returnOrder.getOrderItems());

        return CreateReturnOrderResponse.from(returnOrder.getDocumentNo());
    }

    // 반품 현황
    @Override
    @Transactional(readOnly = true)
    public List<SummaryReturnOrderResponse> getSummaryReturnOrders(SearchSummaryReturnOrderRequest condition, Pageable pageable) {
        return orderRepository.searchSummaryReturnOrders(condition, pageable);
    }
}