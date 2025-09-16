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
import com.domino.smerp.itemorder.dto.response.DetailItemOrderResponse;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.order.dto.request.CreateOrderRequest;
import com.domino.smerp.order.dto.request.OrderSearchRequest;
import com.domino.smerp.order.dto.request.UpdateOrderRequest;
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
import java.math.RoundingMode;
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

    // 공용 조회용 메서드
    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
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

    //전표 생성
    @Transactional
    public String generateDocumentNoWithRetry(LocalDate documentDate) {
        return documentNoGenerator.generate(documentDate, orderRepository::findMaxSequenceByPrefix);
    }

    // 등록용 ItemOrder 생성 메서드
    private ItemOrder toOrderItem(Order order, ItemOrderRequest itemReq) {
        Item item = itemServiceImpl.findItemById(itemReq.getItemId());

        BigDecimal specialPrice = itemReq.getSpecialPrice() != null
                ? itemReq.getSpecialPrice()          // 요청값 있으면 그대로 사용
                : item.getOutboundUnitPrice();       // null이면 품목 단가 고정

        return ItemOrder.builder()
                .order(order)
                .item(item)
                .qty(itemReq.getQty())
                .specialPrice(specialPrice) // 주문 당시 단가 고정
                .build();
    }

    // 주문 등록
    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        // 거래처, 담당자 조회
        Client client = getClientByCompanyName(request.getCompanyName());
        User user = getUserByEmpNo(request.getEmpNo());

        OrderStatus status = OrderStatus.PENDING;

        // 주문일자(documentDate) 기반 documentNo 생성
        LocalDate documentDate = request.getDocumentDate();
        if (documentDate == null) {
            throw new CustomException(ErrorCode.INVALID_ORDER_REQUEST); // 주문일자 없으면 예외
        }
        String documentNo = generateDocumentNoWithRetry(documentDate);

        Instant deliveryDateInstant = request.getDeliveryDate() != null
                ? request.getDeliveryDate().atStartOfDay(ZoneOffset.UTC).toInstant()
                : null;

        // 주문 엔티티 생성
        Order order = Order.builder()
                .client(client)
                .user(user)
                .status(status)
                .deliveryDate(deliveryDateInstant)
                .remark(request.getRemark())
                .documentNo(documentNo)
                .build();

        // 교차 테이블 생성
        request.getItems().forEach(itemReq -> order.addOrderItem(toOrderItem(order, itemReq)));

        // 부모 먼저 저장 → PK 확보
        orderRepository.save(order);

        // 자식들 직접 저장
        itemOrderRepository.saveAll(order.getOrderItems());

        return CreateOrderResponse.from(order);
    }

    // 주문 목록 조회
    @Override
    @Transactional(readOnly = true)
    public PageResponse<ListOrderResponse> getOrders(OrderSearchRequest condition, Pageable pageable) {
        Page<Order> page = orderRepository.searchOrders(condition, pageable);

        Page<ListOrderResponse> dtoPage = page.map(order ->
                ListOrderResponse.builder()
                        .documentNo(order.getDocumentNo())
                        .companyName(order.getClient().getCompanyName())
                        .status(order.getStatus().name())
                        .deliveryDate(order.getDeliveryDate())
                        .userName(order.getUser().getName())
                        .firstItemName(order.getFirstItemName())
                        .otherItemCount(order.getOtherItemCount())
                        .totalAmount(order.getTotalAmount().setScale(2, RoundingMode.HALF_UP))
                        .remark(order.getRemark())
                        .build()
        );

        return PageResponse.from(dtoPage);
    }


    // 주문 상세 조회
    @Override
    @Transactional(readOnly = true)
    public DetailOrderResponse getDetailOrder(Long orderId) {
        Order order = findOrderById(orderId);

        List<DetailItemOrderResponse> itemResponses = order.getOrderItems().stream()
                .map(itemOrder -> DetailItemOrderResponse.builder()
                        .itemId(itemOrder.getItem().getItemId())
                        .itemName(itemOrder.getItem().getName())
                        .specification(itemOrder.getItem().getSpecification())
                        .qty(itemOrder.getQty())
                        .unit(itemOrder.getItem().getUnit())
                        .specialPrice(itemOrder.getSpecialPrice())
                        .supplyAmount(itemOrder.getSupplyAmount().setScale(2, RoundingMode.HALF_UP))
                        .tax(itemOrder.getTax().setScale(2, RoundingMode.HALF_UP))
                        .totalAmount(itemOrder.getTotalAmount().setScale(2, RoundingMode.HALF_UP))
                        .deliveryDate(order.getDeliveryDate().atZone(ZoneOffset.UTC).toLocalDate())
                        .remark(order.getRemark())
                        .build())
                .toList();

        return DetailOrderResponse.builder()
                .documentNo(order.getDocumentNo())
                .orderDate(order.getCreatedAt().atZone(ZoneOffset.UTC).toLocalDate())
                .companyName(order.getClient().getCompanyName())
                .deliveryDate(order.getDeliveryDate().atZone(ZoneOffset.UTC).toLocalDate())
                .userName(order.getUser().getName())
                .remark(order.getRemark())
                .status(order.getStatus().name())
                .items(itemResponses)
                .build();
    }

    // 주문 수정 (PATCH: 없는 건 삭제, 새로 온 건 추가, 기존은 수정)
    @Override
    @Transactional
    public UpdateOrderResponse updateOrder(Long orderId, UpdateOrderRequest request) {
        Order order = findOrderById(orderId);

        User user = getUserByEmpNo(request.getEmpNo());

        // 1. 기존 품목 매핑
        Map<Long, ItemOrder> existingItems = order.getOrderItems().stream()
                .collect(Collectors.toMap(ItemOrder::getItemOrderId, io -> io));

        List<ItemOrder> finalItems = new ArrayList<>();

        // 2. 요청 품목 처리 (수정 / 추가)
        for (UpdateItemOrderRequest itemReq : request.getItems()) {
            if (itemReq.getItemOrderId() != null) {
                ItemOrder existing = existingItems.get(itemReq.getItemOrderId());
                if (existing == null) throw new CustomException(ErrorCode.ITEM_NOT_FOUND);

                existing.updateQty(itemReq.getQty());

                BigDecimal specialPrice = itemReq.getSpecialPrice() != null
                        ? itemReq.getSpecialPrice()
                        : existing.getItem().getOutboundUnitPrice();
                existing.updateSpecialPrice(specialPrice);

                finalItems.add(existing);
                existingItems.remove(itemReq.getItemOrderId());
            } else {
                Item item = itemServiceImpl.findItemById(itemReq.getItemId());
                BigDecimal specialPrice = itemReq.getSpecialPrice() != null
                        ? itemReq.getSpecialPrice()
                        : item.getOutboundUnitPrice();

                ItemOrder newItem = ItemOrder.builder()
                        .order(order)
                        .item(item)
                        .qty(itemReq.getQty())
                        .specialPrice(specialPrice)
                        .build();

                itemOrderRepository.save(newItem);
                order.addOrderItem(newItem);
                finalItems.add(newItem);
            }
        }

        // 3. JSON에 없는 기존 품목 삭제
        for (ItemOrder toDelete : existingItems.values()) {
            itemOrderRepository.delete(toDelete);
            order.getOrderItems().remove(toDelete);
        }

        // 4. 전표번호 갱신 (documentDate 변경 시)
        if (request.getDocumentDate() != null) {
            String newDocNo = documentNoGenerator.generateOrKeep(
                    order.getDocumentNo(),
                    request.getDocumentDate(),
                    orderRepository::findMaxSequenceByPrefix
            );
            order.updateDocumentInfo(newDocNo);
        }

        // 5. 주문 정보 갱신
        Instant newDeliveryDate = request.getDeliveryDate() != null
                ? request.getDeliveryDate().atStartOfDay(ZoneOffset.UTC).toInstant()
                : null;

        order.updateAll(
                newDeliveryDate,
                request.getRemark(),
                request.getStatus(),
                user,
                finalItems
        );

        return UpdateOrderResponse.from(orderRepository.save(order));
    }

    // 주문 삭제

    @Override
    @Transactional
    public DeleteOrderResponse deleteOrder(Long orderId) {
        Order order = findOrderById(orderId);

        orderRepository.delete(order);
        return DeleteOrderResponse.from(order);
    }
}