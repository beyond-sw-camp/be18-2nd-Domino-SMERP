package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderCreateRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderUpdateRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.SearchPurchaseOrderRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.SearchSummaryPurchaseOrderRequest;
import com.domino.smerp.purchase.purchaseorder.dto.response.*;
import com.domino.smerp.purchase.purchaseorder.repository.PurchaseOrderRepository;
import com.domino.smerp.purchase.requestorder.RequestOrder;
import com.domino.smerp.purchase.requestorder.repository.RequestOrderRepository;
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
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

  private final PurchaseOrderRepository purchaseOrderRepository;
  private final RequestOrderRepository requestOrderRepository;

  // ✅ 구매 생성
  @Override
  @Transactional
  public PurchaseOrderCreateResponse createPurchaseOrder(final PurchaseOrderCreateRequest request) {

    // TODO: 실제로는 RequestOrderRepository 에서 roId를 조회해야 함
    RequestOrder requestOrder = requestOrderRepository.findById(request.getRoId())
        .orElseThrow(() -> new EntityNotFoundException("발주 전표를 조회할 수 없습니다."));

    // 자동 계산 로직
      BigDecimal qty = requestOrder.getItems().get(0).getQty();
      BigDecimal inboundUnitPrice = requestOrder.getItems().get(0).getInboundUnitPrice();

      BigDecimal surtax = qty.multiply(inboundUnitPrice).multiply(BigDecimal.valueOf(0.1));
      BigDecimal price = qty.multiply(inboundUnitPrice).add(surtax);

      // 1. 전표번호(documentNo) 결정
      String documentNo = null;   // 블록 밖에서 선언 & 초기화

      if (request.getDocumentNo() != null && !request.getDocumentNo().isBlank()) {
          // 사용자가 직접 입력한 전표번호를 그대로 사용
          documentNo = request.getDocumentNo();
          int lastSeq = purchaseOrderRepository.findLastSequenceByDate(request.getDocumentNo()).orElse(0);
          documentNo = String.format("%s-%d", request.getDocumentNo(), lastSeq + 1);
      } else {
          // 사용자가 입력 안 했을 경우 → 자동 생성
          LocalDate docDate = request.getNewDocDate() != null
                  ? request.getNewDocDate()
                  : LocalDate.now();

          String dateString = docDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
          int lastSeq = purchaseOrderRepository.findLastSequenceByDate(dateString).orElse(0);
          documentNo = String.format("%s-%d", dateString, lastSeq + 1); // 같은 변수에 재할당
      }

    // 엔티티 변환 (빌더 패턴)
    PurchaseOrder entity = PurchaseOrder.builder()
        .requestOrder(requestOrder)
        .qty(qty)
        .inboundUnitPrice(inboundUnitPrice)
        .surtax(surtax)
        .price(price)
        .remark(request.getRemark())
        .documentNo(documentNo)   // 수정: request.getDocumentNo() → documentNo
        .warehouseName(request.getWarehouseName())
        .build();

    // 저장
    PurchaseOrder saved = purchaseOrderRepository.save(entity);

    // 응답 변환 (빌더 패턴)
    return PurchaseOrderCreateResponse.builder()
        .companyName(entity.getRequestOrder().getClient().getCompanyName())
        .empNo(entity.getRequestOrder().getUser().getEmpNo())
        .qty(saved.getQty())
        .inboundUnitPrice(saved.getInboundUnitPrice())
        .documentNo(saved.getDocumentNo())
        .warehouseName(saved.getWarehouseName())
        .createdAt(saved.getCreatedAt())
        .build();
  }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PurchaseOrderGetListResponse> searchPurchaseOrdes(final SearchPurchaseOrderRequest keyword,
                                                                   final Pageable pageable){
      return PageResponse.from(purchaseOrderRepository.searchPurchaseOrder(keyword, pageable).map(
              PurchaseOrderGetListResponse::fromEntity
      ));
    };




  // ✅ 구매 목록 조회 (페이징)
  @Override
  @Transactional(readOnly = true)
  public List<PurchaseOrderGetListResponse> getPurchaseOrders(final int page, final int size) {
    // 생성일자 내림차순 기준 정렬
    PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    return purchaseOrderRepository.findAll(pageable)
        .map(entity -> PurchaseOrderGetListResponse.builder()
            .empNo(entity.getRequestOrder().getUser().getEmpNo())
            .companyName(entity.getRequestOrder().getClient().getCompanyName())
            .qty(entity.getQty())
            .remark(entity.getRemark())
            .documentNo(entity.getDocumentNo())
            .warehouseName(entity.getWarehouseName())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build())
        .toList();
  }

  // ✅ 구매 상세 조회

  @Override
  @Transactional(readOnly = true)
  public PurchaseOrderGetDetailResponse getPurchaseOrderById(final Long poId) {
    PurchaseOrder entity = purchaseOrderRepository.findByIdWithRequestOrderAndItems(poId)
        .orElseThrow(() -> new EntityNotFoundException("구매 전표를 조회할 수 없습니다." + poId));

      // ✅ 품목 리스트 변환
      List<PurchaseOrderDetailItemResponse> items = entity.getRequestOrder().getItems().stream()
              .map(itemRequestOrder -> PurchaseOrderDetailItemResponse.builder()
                      .itemName(itemRequestOrder.getItem().getName())
                      .qty(itemRequestOrder.getQty())
                      .inboundUnitPrice(itemRequestOrder.getInboundUnitPrice())
                      .supplyAmount(itemRequestOrder.getQty().multiply(itemRequestOrder.getInboundUnitPrice()))
                      .build())
              .toList();

    return PurchaseOrderGetDetailResponse.builder()
        .empNo(entity.getRequestOrder().getUser().getEmpNo())
        .companyName(entity.getRequestOrder().getClient().getCompanyName())
//        .qty(entity.getQty())
//        .inboundUnitPrice(entity.getInboundUnitPrice())
//        .surtax(entity.getSurtax())
//        .price(entity.getPrice())
        .remark(entity.getRemark())
        .documentNo(entity.getDocumentNo())
        .warehouseName(entity.getWarehouseName())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .items(items)
        .build();
  }

    // ✅ 구매 수정
    @Override
    @Transactional
    public PurchaseOrderUpdateResponse updatePurchaseOrder(final Long poId,
                                                           final PurchaseOrderUpdateRequest request) {
        PurchaseOrder entity = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new EntityNotFoundException("구매 전표를 조회할 수 없습니다."));

        if (request.getWarehouseName() != null) {
            entity.updateWarehouseName(request.getWarehouseName());
        }
        if (request.getRemark() != null) {
            entity.updateRemark(request.getRemark());
        }
        // documentNo 변경 요청이 있을 경우
        if (request.getNewDocDate() != null) {
            LocalDate newDate = request.getNewDocDate();
            String dateString = newDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

            // Repository 호출해서 마지막 시퀀스 조회
            int lastSeq = purchaseOrderRepository.findLastSequenceByDate(dateString)
                    .orElse(0);

            entity.updateDocumentNo(newDate, lastSeq + 1);
        }

        return PurchaseOrderUpdateResponse.builder()
                .empNo(entity.getRequestOrder().getUser().getEmpNo())
                .companyName(entity.getRequestOrder().getClient().getCompanyName())
                .warehouseName(entity.getWarehouseName())
                .qty(entity.getQty())
                .inboundUnitPrice(entity.getInboundUnitPrice())
//                .specialPrice(entity.getSpecialPrice())
                .remark(entity.getRemark())
                .documentNo(entity.getDocumentNo())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


  // ✅ 구매 삭제 (소프트 삭제)
  @Override
  @Transactional
  public PurchaseOrderDeleteResponse deletePurchaseOrder(final Long poId) {
    PurchaseOrder entity = purchaseOrderRepository.findById(poId)
        .orElseThrow(() -> new EntityNotFoundException("구매 전표를 조회할 수 없습니다."));

    entity.delete(); // 엔티티 도메인 메서드: isDeleted=true, updatedAt=now

    return PurchaseOrderDeleteResponse.builder()
        .message("구매 전표가 삭제되었습니다.")
        .build();
  }

  // ✅ 구매 현황
  @Override
  @Transactional(readOnly = true)
  public List<SummaryPurchaseOrderResponse> getSummaryPurchaseOrders(SearchSummaryPurchaseOrderRequest condition, Pageable pageable) {
      return purchaseOrderRepository.searchSummaryPurchaseOrders(condition, pageable);
  }

}
