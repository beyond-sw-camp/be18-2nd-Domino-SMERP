package com.domino.smerp.purchase.requestorder;

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

    // ✅ 발주 생성
    @Override
    @Transactional
    public RequestOrderCreateResponse createRequestOrder(final RequestOrderCreateRequest request) {
        RequestPurchaseOrder requestPurchaseOrder = null;

        // case 1: 구매요청 기반 발주
        if (request.getRpoId() != null) {
            requestPurchaseOrder = requestPurchaseOrderRepository.findById(request.getRpoId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "구매요청을 찾을 수 없습니다. id=" + request.getRpoId()));

            if (requestPurchaseOrder.getStatus() != RequestPurchaseOrderStatus.PENDING) {
                throw new IllegalStateException("PENDING 상태의 구매요청만 발주 생성 가능합니다.");
            }

            // 구매요청 상태 변경 → APPROVED
            requestPurchaseOrder.updateStatus(RequestPurchaseOrderStatus.APPROVED);
        }

        // 발주 엔티티 변환
        RequestOrder entity = RequestOrder.builder()
                .requestPurchaseOrder(requestPurchaseOrder) // null 가능 (독립 발주)
                .clientId(request.getClientId())
                .userId(request.getUserId())
                .deliveryDate(request.getDeliveryDate())
                .remark(request.getRemark())
                .status(RequestOrderStatus.PENDING)
                .documentNo(request.getDocumentNo())
                .build();

        // 저장
        RequestOrder saved = requestOrderRepository.save(entity);

    // 품목 교차 테이블 등록 + 응답용 리스트 생성
    List<RequestOrderCreateResponse.ItemDetail> itemDetails = request.getItems().stream()
            .map(itemDto -> {
                Item itemEntity = itemRepository.findById(itemDto.getItemId())
                        .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다. id=" + itemDto.getItemId()));

                ItemRequestOrder crossed = ItemRequestOrder.builder()
                        .requestOrder(saved)
                        .item(itemEntity)
                        .qty(itemDto.getQty())
                        .inboundUnitPrice(itemDto.getInboundUnitPrice())
                        .build();

                itemRequestOrderRepository.save(crossed);

                // 응답용 DTO 리턴
                return new RequestOrderCreateResponse.ItemDetail(
                        itemEntity.getItemId(),
                        itemDto.getQty(),
                        itemDto.getInboundUnitPrice()
                );
            })
            .toList();

        // 응답 변환
        return RequestOrderCreateResponse.builder()
                .roId(saved.getRoId())
                .clientId(saved.getClientId())
                .userId(saved.getUserId())
                .deliveryDate(saved.getDeliveryDate())
                .documentNo(saved.getDocumentNo())
                .createdAt(saved.getCreatedAt())
                .items(itemDetails)
                .build();
    }

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
                            .roId(entity.getRoId())
                            .clientId(entity.getClientId())
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
                        .build()
                )
                .toList();

        return RequestOrderGetDetailResponse.builder()
                .roId(entity.getRoId())
                .userId(entity.getUserId())
                .clientId(entity.getClientId())
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
                .orElseThrow(() -> new EntityNotFoundException("발주 전표를 조회할 수 없습니다. id=" + roId));

        // 엔티티 도메인 메서드 활용
        entity.updateDeliveryDate(request.getDeliveryDate());
        entity.updateRemark(request.getRemark());

        if (request.getStatus() != null) {
            entity.updateStatus(RequestOrderStatus.valueOf(request.getStatus().toUpperCase()));
        }

        // documentNo 변경 요청이 있을 경우
        if (request.getNewDocDate() != null) {
            LocalDate newDate = request.getNewDocDate();
            String dateString = newDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            int lastSeq = requestOrderRepository.findLastSequenceByDate(dateString).orElse(0);
            entity.updateDocumentNo(newDate, lastSeq + 1);
        }

        return RequestOrderUpdateResponse.builder()
                .roId(entity.getRoId())
                .deliveryDate(entity.getDeliveryDate())
                .remark(entity.getRemark())
                .status(entity.getStatus().name())
                .documentNo(entity.getDocumentNo())
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
                .roId(entity.getRoId())
                .isDeleted(entity.isDeleted())
                .deletedAt(entity.getUpdatedAt())
                .message("발주 전표가 삭제되었습니다.")
                .build();
    }
}
