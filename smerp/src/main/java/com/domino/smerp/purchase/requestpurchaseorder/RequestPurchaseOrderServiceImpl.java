package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.repository.ItemRepository;
import com.domino.smerp.purchase.itemrequestpurchaseorder.ItemRequestPurchaseOrder;
import com.domino.smerp.purchase.itemrequestpurchaseorder.ItemRequestPurchaseOrderRepository;
import com.domino.smerp.purchase.itemrequestpurchaseorder.dto.request.ItemRequestPurchaseOrderDto;
import com.domino.smerp.purchase.requestpurchaseorder.constants.RequestPurchaseOrderStatus;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.RequestPurchaseOrderCreateRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.RequestPurchaseOrderUpdateRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.SearchRequestPurchaseOrderRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.response.*;
import com.domino.smerp.purchase.requestpurchaseorder.repository.RequestPurchaseOrderRepository;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestPurchaseOrderServiceImpl implements RequestPurchaseOrderService {

    private final RequestPurchaseOrderRepository requestPurchaseOrderRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestPurchaseOrderRepository itemRequestPurchaseOrderRepository;
    private final UserRepository userRepository;

    // ✅ 구매요청 생성
    @Override
    @Transactional
    public RequestPurchaseOrderCreateResponse createRequestPurchaseOrder(final RequestPurchaseOrderCreateRequest request) {
        // empNo 기반으로 User 조회
        User userEntity = userRepository.findByEmpNo(request.getEmpNo())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. empNo=" + request.getEmpNo()));

        // ===== 전표번호 생성 =====
        String documentNo;
        if (request.getDocumentNo() != null && !request.getDocumentNo().isBlank()) {
            int lastSeq = requestPurchaseOrderRepository.findLastSequenceByDate(request.getDocumentNo()).orElse(0);
            documentNo = String.format("%s-%d", request.getDocumentNo(), lastSeq + 1);
        } else {
            LocalDate docDate = LocalDate.now();
            String dateString = docDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            int lastSeq = requestPurchaseOrderRepository.findLastSequenceByDate(dateString).orElse(0);
            documentNo = String.format("%s-%d", dateString, lastSeq + 1);
        }

        RequestPurchaseOrder entity = RequestPurchaseOrder.builder()
                .user(userEntity)
                .deliveryDate(request.getDeliveryDate())
                .remark(request.getRemark())
                .status(RequestPurchaseOrderStatus.PENDING)
                .documentNo(documentNo)
                .build();

        RequestPurchaseOrder saved = requestPurchaseOrderRepository.save(entity);

        List<RequestPurchaseOrderCreateResponse.ItemDetail> itemDetails = request.getItems().stream()
                .map(itemDto -> {
                    if (itemDto.getInboundUnitPrice() == null || itemDto.getInboundUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                        throw new IllegalArgumentException("단가가 올바르지 않습니다.");
                    }

                    Item itemEntity = itemRepository.findById(itemDto.getItemId())
                            .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다."));

                    ItemRequestPurchaseOrder crossed = ItemRequestPurchaseOrder.builder()
                            .requestPurchaseOrder(saved)
                            .item(itemEntity)
                            .qty(itemDto.getQty())
                            .inboundUnitPrice(itemDto.getInboundUnitPrice())
                            .specialPrice(itemDto.getSpecialPrice() != null
                                    ? itemDto.getSpecialPrice()
                                    : itemEntity.getInboundUnitPrice())   // ★ 특별단가 우선 적용
                            .build();

                    itemRequestPurchaseOrderRepository.save(crossed);

                    return new RequestPurchaseOrderCreateResponse.ItemDetail(
                            itemEntity.getItemId(),
                            itemDto.getQty(),
                            itemDto.getInboundUnitPrice(),
                            crossed.getSpecialPrice()
                    );
                })
                .toList();

        return RequestPurchaseOrderCreateResponse.builder()
                .documentNo(saved.getDocumentNo())
                .empNo(saved.getUser().getEmpNo())
                .status(saved.getStatus())
                .message("구매요청이 성공적으로 등록되었습니다.")
                .items(itemDetails)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RequestPurchaseOrderGetListResponse> searchRequestPurchaseOrders(
            final SearchRequestPurchaseOrderRequest keyword,
            final Pageable pageable
    ) {
        return PageResponse.from(
                requestPurchaseOrderRepository.searchRequestPurchaseOrder(keyword, pageable)
                        .map(RequestPurchaseOrderGetListResponse::fromEntity)
        );
    }


    // ✅ 구매요청 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<RequestPurchaseOrderGetListResponse> getRequestPurchaseOrders(final int page, final int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return requestPurchaseOrderRepository.findAll(pageable)
                .map(entity -> {
                    List<ItemRequestPurchaseOrder> items = entity.getItems();

                    String itemName;
                    BigDecimal totalQty;

                    if (items.isEmpty()) {
                        itemName = null;
                        totalQty = BigDecimal.ZERO;
                    } else if (items.size() == 1) {
                        itemName = items.get(0).getItem().getName();
                        totalQty = items.get(0).getQty();
                    } else {
                        itemName = items.get(0).getItem().getName() + " 외 " + (items.size() - 1) + "건";
                        totalQty = items.stream()
                                .map(ItemRequestPurchaseOrder::getQty)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                    }

                    return RequestPurchaseOrderGetListResponse.builder()
                            .documentNo(entity.getDocumentNo())
                            .empNo(entity.getUser().getEmpNo())
                            .itemName(itemName)
                            .totalQty(totalQty)
                            .deliveryDate(entity.getDeliveryDate())
                            .status(entity.getStatus().name())
                            .createdAt(entity.getCreatedAt())
                            .build();
                })
                .toList();
    }

    // ✅ 구매요청 상세 조회
    @Override
    @Transactional(readOnly = true)
    public RequestPurchaseOrderGetDetailResponse getRequestPurchaseOrderById(final Long rpoId) {
        RequestPurchaseOrder entity = requestPurchaseOrderRepository.findById(rpoId)
                .orElseThrow(() -> new EntityNotFoundException("구매요청 전표를 조회할 수 없습니다."));

        List<ItemRequestPurchaseOrderDto> itemDtos = entity.getItems().stream()
                .map(item -> ItemRequestPurchaseOrderDto.builder()
                        .itemId(item.getItem().getItemId())
                        .name(item.getItem().getName())
                        .qty(item.getQty())
                        .inboundUnitPrice(item.getInboundUnitPrice())
                        .specialPrice(item.getSpecialPrice())
                        .build()
                )
                .toList();

        return RequestPurchaseOrderGetDetailResponse.builder()
                .documentNo(entity.getDocumentNo())
                .empNo(entity.getUser().getEmpNo())
                .deliveryDate(entity.getDeliveryDate())
                .status(entity.getStatus().name())
                .remark(entity.getRemark())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .items(itemDtos)
                .build();
    }

    // ✅ 구매요청 수정
    @Override
    @Transactional
    public RequestPurchaseOrderUpdateResponse updateRequestPurchaseOrder(final Long rpoId,
                                                                         final RequestPurchaseOrderUpdateRequest request) {
        RequestPurchaseOrder entity = requestPurchaseOrderRepository.findById(rpoId)
                .orElseThrow(() -> new EntityNotFoundException("구매요청 전표를 조회할 수 없습니다."));

        entity.updateDeliveryDate(request.getDeliveryDate());
        entity.updateRemark(request.getRemark());

        if (request.getStatus() != null) {
            entity.updateStatus(RequestPurchaseOrderStatus.valueOf(request.getStatus().toUpperCase()));
        }

        if (request.getNewDocDate() != null) {
            LocalDate newDate = request.getNewDocDate();
            String dateString = newDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            int lastSeq = requestPurchaseOrderRepository.findLastSequenceByDate(dateString).orElse(0);
            entity.updateDocumentNo(newDate, lastSeq + 1);
        }

        // ✅ items 수정 (기존 전체 삭제 후 새로 등록)
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            // 기존 아이템 삭제
            itemRequestPurchaseOrderRepository.deleteByRequestPurchaseOrder(entity);

            // 새 아이템 등록
            request.getItems().forEach(itemDto -> {
                if (itemDto.getQty() == null || itemDto.getQty().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("수량이 올바르지 않습니다.");
                }

                if (itemDto.getInboundUnitPrice() == null || itemDto.getInboundUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("입고단가가 올바르지 않습니다.");
                }

                Item itemEntity = itemRepository.findById(itemDto.getItemId())
                        .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다. id=" + itemDto.getItemId()));

                ItemRequestPurchaseOrder crossed = ItemRequestPurchaseOrder.builder()
                        .requestPurchaseOrder(entity)
                        .item(itemEntity)
                        .qty(itemDto.getQty())
                        .inboundUnitPrice(itemDto.getInboundUnitPrice())
                        .specialPrice(itemDto.getSpecialPrice() != null
                                ? itemDto.getSpecialPrice()
                                : itemEntity.getInboundUnitPrice()) // ★ 특별단가 우선 적용
                        .build();

                itemRequestPurchaseOrderRepository.save(crossed);
            });
        }

        // items 변환
        List<ItemRequestPurchaseOrderDto> itemDtos = entity.getItems().stream()
                .map(item -> ItemRequestPurchaseOrderDto.builder()
                        .itemId(item.getItem().getItemId())
                        .name(item.getItem().getName())
                        .qty(item.getQty())
                        .inboundUnitPrice(item.getInboundUnitPrice())
                        .specialPrice(item.getSpecialPrice()) // ★ 응답에 특별단가 추가
                        .build()
                )
                .toList();

        return RequestPurchaseOrderUpdateResponse.builder()
                .documentNo(entity.getDocumentNo())
                .empNo(entity.getUser().getEmpNo())
                .deliveryDate(entity.getDeliveryDate())
                .remark(entity.getRemark())
                .status(entity.getStatus().name())
                .updatedAt(entity.getUpdatedAt())
                .items(itemDtos)
                .build();
    }

    // ✅ 구매요청 삭제
    @Override
    @Transactional
    public RequestPurchaseOrderDeleteResponse deleteRequestPurchaseOrder(final Long rpoId) {
        RequestPurchaseOrder entity = requestPurchaseOrderRepository.findById(rpoId)
                .orElseThrow(() -> new EntityNotFoundException("조회할 수 없습니다."));

        entity.delete();

        return RequestPurchaseOrderDeleteResponse.builder()
                .message("구매요청 전표가 삭제되었습니다.")
                .build();
    }
}
