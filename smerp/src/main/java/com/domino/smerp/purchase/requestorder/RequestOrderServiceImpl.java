package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.client.Client;
import com.domino.smerp.client.ClientRepository;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.repository.ItemRepository;
import com.domino.smerp.purchase.itemrequestorder.ItemRequestOrder;
import com.domino.smerp.purchase.itemrequestorder.ItemRequestOrderRepository;
import com.domino.smerp.purchase.itemrequestorder.dto.request.ItemRequestOrderDto;
import com.domino.smerp.purchase.requestorder.constants.RequestOrderStatus;
import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderCreateRequest;
import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderUpdateRequest;
import com.domino.smerp.purchase.requestorder.dto.response.*;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrderRepository;
import com.domino.smerp.purchase.requestpurchaseorder.constants.RequestPurchaseOrderStatus;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestOrderServiceImpl implements RequestOrderService {

    private final RequestOrderRepository requestOrderRepository;
    private final RequestPurchaseOrderRepository requestPurchaseOrderRepository;
    private final ItemRepository itemRepository; // 품목 조회용
    private final ItemRequestOrderRepository itemRequestOrderRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    // ✅ 발주 생성
    @Override
    @Transactional
    public RequestOrderCreateResponse createRequestOrder(final RequestOrderCreateRequest request) {
        RequestPurchaseOrder requestPurchaseOrder = null;

        // case 1: 구매요청 기반 발주
        if (request.getRpoId() != null) {
            requestPurchaseOrder = requestPurchaseOrderRepository.findById(request.getRpoId())
                    .orElseThrow(() -> new EntityNotFoundException("구매요청을 찾을 수 없습니다. id=" + request.getRpoId()));

            if (requestPurchaseOrder.getStatus() != RequestPurchaseOrderStatus.PENDING) {
                throw new IllegalStateException("PENDING 상태의 구매요청만 발주 생성 가능합니다.");
            }

            // 구매요청 상태 변경 → APPROVED
            requestPurchaseOrder.updateStatus(RequestPurchaseOrderStatus.APPROVED);
        }

        // ===== 전표번호 생성 =====
        String documentNo;
        if (request.getDocumentNo() != null && !request.getDocumentNo().isBlank()) {
            // 사용자가 날짜만 입력했을 경우 (yyyy/MM/dd)
            String dateString = request.getDocumentNo();
            int lastSeq = requestOrderRepository.findLastSequenceByDate(dateString).orElse(0);
            documentNo = String.format("%s-%d", dateString, lastSeq + 1);
        } else {
            // 사용자가 입력 안 했을 경우 → 오늘 날짜 기준
            LocalDate docDate = LocalDate.now();
            String dateString = docDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            int lastSeq = requestOrderRepository.findLastSequenceByDate(dateString).orElse(0);
            documentNo = String.format("%s-%d", dateString, lastSeq + 1);
        }

        // 사번 기반으로 사용자 조회
        User userEntity = userRepository.findByEmpNo(request.getEmpNo())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. empNo=" + request.getEmpNo()));

// 거래처 이름 기반으로 조회
        Client clientEntity = clientRepository.findByCompanyName(request.getCompanyName())
                .orElseThrow(() -> new EntityNotFoundException("거래처를 찾을 수 없습니다. name=" + request.getCompanyName()));


        // 발주 엔티티 변환
        RequestOrder entity = RequestOrder.builder()
                .requestPurchaseOrder(requestPurchaseOrder) // null 가능 (독립 발주)
                .user(userEntity)
                .client(clientEntity)
                .deliveryDate(request.getDeliveryDate())
                .remark(request.getRemark())
                .status(RequestOrderStatus.PENDING)
                .documentNo(documentNo)
                .build();

        // 저장
        RequestOrder saved = requestOrderRepository.save(entity);

        // 품목 교차 테이블 등록 + 응답용 리스트 생성
        List<RequestOrderCreateResponse.ItemDetail> itemDetails = request.getItems().stream()
                .map(itemDto -> {
                    // 단가 유효성 검증 추가
                    if (itemDto.getInboundUnitPrice() == null || itemDto.getInboundUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                        throw new IllegalArgumentException("단가가 올바르지 않습니다. itemId=" + itemDto.getItemId());
                    }
                    Item itemEntity = itemRepository.findById(itemDto.getItemId())
                            .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다. id=" + itemDto.getItemId()));

                    ItemRequestOrder crossed = ItemRequestOrder.builder()
                            .requestOrder(saved)
                            .item(itemEntity)
                            .qty(itemDto.getQty())
                            .inboundUnitPrice(itemDto.getInboundUnitPrice())
                            .specialPrice(itemDto.getSpecialPrice() != null
                                    ? itemDto.getSpecialPrice()
                                    : itemEntity.getInboundUnitPrice())   // ★ 특별단가 우선 적용
                            .build();

                    itemRequestOrderRepository.save(crossed);

                    // 응답용 DTO 리턴
                    return new RequestOrderCreateResponse.ItemDetail(
                            itemEntity.getItemId(),
                            itemEntity.getName(),
                            itemDto.getQty(),
                            itemDto.getInboundUnitPrice(),
                            crossed.getSpecialPrice()
                    );
                })
                .toList();

        // 응답 변환
        return RequestOrderCreateResponse.builder()
                .empNo(entity.getUser().getEmpNo())
                .companyName(entity.getClient().getCompanyName())
                .deliveryDate(saved.getDeliveryDate())
                .documentNo(saved.getDocumentNo())

                .createdAt(saved.getCreatedAt())
                .items(itemDetails)
                .build();
    }

    // 발주 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<RequestOrderGetListResponse> getRequestOrders(final int page, final int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return requestOrderRepository.findAll(pageable)
                .map(entity -> {
                    List<ItemRequestOrder> items = entity.getItems();

                    String itemName;
                    BigDecimal totalQty;

                    if (items.isEmpty()) {
                        itemName = null;
                        totalQty = BigDecimal.ZERO;
                    } else if (items.size() == 1) {
                        itemName = items.get(0).getItem().getName();   // Item 엔티티에서 getName()
                        totalQty = items.get(0).getQty();
                    } else {
                        itemName = items.get(0).getItem().getName() + " 외 " + (items.size() - 1) + "건";
                        totalQty = items.stream()
                                .map(ItemRequestOrder::getQty)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    }

                    return RequestOrderGetListResponse.builder()
                            .empNo(entity.getUser().getEmpNo())
                            .companyName(entity.getClient().getCompanyName())
                            .itemName(itemName)
                            .totalQty(totalQty)
                            .deliveryDate(entity.getDeliveryDate())
                            .status(entity.getStatus().name())
                            .documentNo(entity.getDocumentNo())
                            .createdAt(entity.getCreatedAt())
                            .build();
                })
                .toList();
    }


    // ✅ 발주 상세 조회
    @Override
    @Transactional(readOnly = true)
    public RequestOrderGetDetailResponse getRequestOrderById(final Long roId) {
        RequestOrder entity = requestOrderRepository.findById(roId)
                .orElseThrow(() -> new EntityNotFoundException("발주 전표를 조회할 수 없습니다. id=" + roId));

        List<ItemRequestOrderDto> itemDtos = entity.getItems().stream()
                .map(item -> ItemRequestOrderDto.builder()
                        .itemId(item.getItem().getItemId())
                        .name(item.getItem().getName())
                        .qty(item.getQty())
                        .inboundUnitPrice(item.getInboundUnitPrice())
                        .specialPrice(item.getSpecialPrice())
                        .build()
                )
                .toList();

        return RequestOrderGetDetailResponse.builder()
                .empNo(entity.getUser().getEmpNo())
                .companyName(entity.getClient().getCompanyName())
                .deliveryDate(entity.getDeliveryDate())
                .status(entity.getStatus().name())
                .remark(entity.getRemark())
                .documentNo(entity.getDocumentNo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .items(itemDtos)
                .build();
    }

    // ✅ 발주 수정
    @Override
    @Transactional
    public RequestOrderUpdateResponse updateRequestOrder(final Long roId,
                                                         final RequestOrderUpdateRequest request) {
        RequestOrder entity = requestOrderRepository.findById(roId)
                .orElseThrow(() -> new EntityNotFoundException("발주 전표를 조회할 수 없습니다. id="));

        // ====== 기본 필드 수정 ======
        entity.updateDeliveryDate(request.getDeliveryDate());
        entity.updateRemark(request.getRemark());

        if (request.getStatus() != null) {
            entity.updateStatus(RequestOrderStatus.valueOf(request.getStatus().toUpperCase()));
        }

        // ====== 전표번호 변경 ======
        if (request.getNewDocDate() != null) {
            LocalDate newDate = request.getNewDocDate();
            String dateString = newDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            int lastSeq = requestOrderRepository.findLastSequenceByDate(dateString).orElse(0);
            entity.updateDocumentNo(newDate, lastSeq + 1);
        }

        // ====== 품목 수정 로직 ======
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            // 기존 품목 전체 삭제
            itemRequestOrderRepository.deleteByRequestOrder(entity);

            // 새 품목 등록
            request.getItems().forEach(itemDto -> {
                if (itemDto.getInboundUnitPrice() == null || itemDto.getInboundUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("단가가 올바르지 않습니다.");
                }

                Item itemEntity = itemRepository.findById(itemDto.getItemId())
                        .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다. id=" + itemDto.getItemId()));

                ItemRequestOrder crossed = ItemRequestOrder.builder()
                        .requestOrder(entity)
                        .item(itemEntity)
                        .qty(itemDto.getQty())
                        .inboundUnitPrice(itemDto.getInboundUnitPrice())
                        .specialPrice(itemDto.getSpecialPrice() != null
                                ? itemDto.getSpecialPrice()
                                : itemEntity.getInboundUnitPrice()) // ★ 특별단가 우선 적용
                        .build();

                itemRequestOrderRepository.save(crossed);
            });
        }

        // ====== 응답용 items 변환 ======
        List<ItemRequestOrderDto> itemDetails = itemRequestOrderRepository.findByRequestOrder(entity).stream()
                .map(item -> ItemRequestOrderDto.builder()
                        .itemId(item.getItem().getItemId())
                        .name(item.getItem().getName())
                        .qty(item.getQty())
                        .inboundUnitPrice(item.getInboundUnitPrice())
                        .specialPrice(item.getSpecialPrice())
                        .build()
                )
                .toList();

        // ====== 응답 반환 ======
        return RequestOrderUpdateResponse.builder()
                .documentNo(entity.getDocumentNo())
                .empNo(entity.getUser().getEmpNo())
                .companyName(entity.getClient().getCompanyName())
                .deliveryDate(entity.getDeliveryDate())
                .remark(entity.getRemark())
                .status(entity.getStatus().name())
                .items(itemDetails)
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    // ✅ 발주 삭제 (소프트 삭제)
    @Override
    @Transactional
    public RequestOrderDeleteResponse deleteRequestOrder(final Long roId) {
        RequestOrder entity = requestOrderRepository.findById(roId)
                .orElseThrow(() -> new EntityNotFoundException("조회할 수 없습니다. id=" + roId));

        entity.delete(); // 소프트 삭제

        return RequestOrderDeleteResponse.builder()
                .message("발주 전표가 삭제되었습니다.")
                .build();
    }
}