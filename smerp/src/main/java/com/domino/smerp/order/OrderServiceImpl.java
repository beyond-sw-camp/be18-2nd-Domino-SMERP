package com.domino.smerp.order;

import com.domino.smerp.client.Client;
import com.domino.smerp.client.ClientRepository;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemServiceImpl;
import com.domino.smerp.itemorder.ItemOrderCrossedTable;
import com.domino.smerp.itemorder.dto.request.ItemOrderRequest;
import com.domino.smerp.itemorder.dto.response.DetailItemOrderResponse;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.order.dto.request.CreateOrderRequest;
import com.domino.smerp.order.dto.request.UpdateOrderRequest;
import com.domino.smerp.order.dto.response.*;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ItemServiceImpl itemServiceImpl;

    private Client getClientByCompanyName(String companyName) {
        return clientRepository.findByCompanyName(companyName)
                .orElseThrow(() -> new CustomException(ErrorCode.CLIENT_NOT_FOUND));
    }

    private User getUserByEmpNo(String empNo) {
        return userRepository.findByEmpNo(empNo)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    //전표번호 생성 (yyyy/MM/dd-순번)
    private String generateDocumentNo(LocalDate documentDate) {
        String datePart = documentDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        for (int i = 0; i < 3; i++) { // 최대 3번 재시도
            long count = orderRepository.countByDocumentNoStartingWith(datePart);
            String candidate = datePart + "-" + (count + 1);
            try {
                return candidate;
            } catch (DataIntegrityViolationException e) {
                // 유니크 충돌 → 재시도
            }
        }
        throw new CustomException(ErrorCode.INVALID_ORDER_REQUEST); // 실패 시
    }

    // ItemOrderCrossedTable 생성 편의 메서드
    private ItemOrderCrossedTable toOrderItem(Order order, ItemOrderRequest itemReq) {
        Item item = itemServiceImpl.findItemById(itemReq.getItemId());
        return ItemOrderCrossedTable.builder()
                .order(order)
                .item(item)
                .qty(itemReq.getQty())
                .specialPrice(item.getOutboundUnitPrice()) // 주문 당시 단가 고정
                .build();
    }

    // 주문 등록
    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        // 거래처, 담당자 조회
        Client client = getClientByCompanyName(request.getCompanyName());
        User user = getUserByEmpNo(request.getEmpNo());

        OrderStatus status = (request.getStatus() != null) ? request.getStatus() : OrderStatus.PENDING;

        // 주문일자(documentDate) 기반 documentNo 생성
        LocalDate documentDate = request.getDocumentDate();
        if (documentDate == null) {
            throw new CustomException(ErrorCode.INVALID_ORDER_REQUEST); // 주문일자 없으면 예외
        }
        String documentNo = generateDocumentNo(documentDate);

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

        return CreateOrderResponse.from(orderRepository.save(order));
}

    // 주문 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<ListOrderResponse> getOrders() {
        return orderRepository.findAll().stream()
                .map(order -> ListOrderResponse.builder()
                        .documentNo(order.getDocumentNo())
                        .companyName(order.getClient().getCompanyName())
                        .status(order.getStatus().name())
                        .deliveryDate(order.getDeliveryDate())
                        .userName(order.getUser().getName())
                        .firstItemName(order.getFirstItemName())
                        .otherItemCount(order.getOtherItemCount())
                        .totalAmount(order.getTotalAmount().setScale(2,RoundingMode.HALF_UP))
                        .remark(order.getRemark())
                        .build()
                )
                .toList();
    }

    // 주문 상세 조회
    @Override
    @Transactional(readOnly = true)
    public DetailOrderResponse getDetailOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        List<DetailItemOrderResponse> itemResponses = order.getOrderItems().stream()
                .map(itemOrder -> DetailItemOrderResponse.builder()
                        .itemCode(itemOrder.getItem().getItemId())
                        .itemName(itemOrder.getItem().getName())
                        .specification(itemOrder.getItem().getSpecification())
                        .qty(itemOrder.getQty())
                        .unit(itemOrder.getItem().getUnit())
                        .specialPrice(itemOrder.getSpecialPrice())
                        .supplyAmount(itemOrder.getSupplyAmount().setScale(2, RoundingMode.HALF_UP)) //  엔티티 메서드 호출
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
                .deliveryDate(order.getDeliveryDate()
                        .atZone(ZoneOffset.UTC).toLocalDate())
                .userName(order.getUser().getName())
                .remark(order.getRemark())
                .status(order.getStatus().name())
                .items(itemResponses)
                .build();
    }

    // 주문 변경
    @Override
    @Transactional
    public UpdateOrderResponse updateOrder(Long orderId, UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        User user = getUserByEmpNo(request.getEmpNo());

        List<ItemOrderCrossedTable> newOrderItems = request.getItems().stream()
                .map(itemReq -> toOrderItem(order, itemReq))
                .toList();

        // 납기일 변환
        Instant newDeliveryDateInstant = request.getDeliveryDate() != null
                ? request.getDeliveryDate().atStartOfDay(ZoneOffset.UTC).toInstant()
                : null;

        // documentDate 변경 시 새 documentNo 생성
        String newDocumentNo = order.getDocumentNo(); // 기본값: 기존 번호 유지
        if (request.getDocumentDate() != null) {
            String newDatePart = request.getDocumentDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String oldDatePart = order.getDocumentNo().split("-")[0]; // ex) "2025/09/09"
            // 날짜가 바뀐 경우에만 새 번호 생성
            if (!newDatePart.equals(oldDatePart)) {
                newDocumentNo = generateDocumentNo(request.getDocumentDate());
            }
        }

        // 주문 전체 업데이트
        order.updateAll(
                request.getDocumentDate() != null
                        ? request.getDocumentDate().atStartOfDay(ZoneOffset.UTC).toInstant()
                        : order.getCreatedAt(),   // documentDate → Instant 변환
                newDeliveryDateInstant,
                request.getRemark(),
                request.getStatus(),
                user,
                newOrderItems
        );

        // 전표번호 갱신
        order.updateDocumentInfo(newDocumentNo);

        return UpdateOrderResponse.from(orderRepository.save(order));
    }


    // 주문 삭제

    @Override
    @Transactional
    public DeleteOrderResponse deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        orderRepository.delete(order);
        return DeleteOrderResponse.from(order);
    }
}
