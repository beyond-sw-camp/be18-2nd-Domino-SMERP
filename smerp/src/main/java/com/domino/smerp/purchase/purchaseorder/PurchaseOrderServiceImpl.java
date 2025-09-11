package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderCreateRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderUpdateRequest;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderCreateResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderDeleteResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderGetDetailResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderGetListResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderUpdateResponse;
import com.domino.smerp.purchase.requestorder.RequestOrder;
import com.domino.smerp.purchase.requestorder.RequestOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    // 예시: RequestOrder requestOrder = requestOrderRepository.findById(request.getRoId())
    //         .orElseThrow(() -> new EntityNotFoundException("발주 전표를 찾을 수 없습니다. id=" + request.getRoId()));

    // RequestOrder requestOrder = null; // 임시 placeholder

    RequestOrder requestOrder = requestOrderRepository.findById(request.getRoId())
        .orElseThrow(() -> new EntityNotFoundException("발주 전표를 조회할 수 없습니다." + request.getRoId()));

    // 엔티티 변환 (빌더 패턴)
    PurchaseOrder entity = PurchaseOrder.builder()
        .requestOrder(requestOrder)
        .qty(request.getQty())
        .surtax(request.getSurtax())
        .price(request.getPrice())
        .remark(request.getRemark())
        .documentNo(request.getDocumentNo())
        .build();

    // 저장
    PurchaseOrder saved = purchaseOrderRepository.save(entity);

    // 응답 변환 (빌더 패턴)
    return PurchaseOrderCreateResponse.builder()
        .poId(saved.getPoId())
        .roId(saved.getRequestOrder() != null ? saved.getRequestOrder().getRoId() : null)
        .qty(saved.getQty())
        .documentNo(saved.getDocumentNo())
        .createdAt(saved.getCreatedAt())
        .build();
  }

  // ✅ 구매 목록 조회 (페이징)

  @Override
  @Transactional(readOnly = true)
  public List<PurchaseOrderGetListResponse> getPurchaseOrders(final int page, final int size) {
    // 생성일자 내림차순 기준 정렬
    PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
    return purchaseOrderRepository.findAll(pageable)
        .map(entity -> PurchaseOrderGetListResponse.builder()
            .poId(entity.getPoId())
//            .roId(entity.getRequestOrder().getRoId())
            .qty(entity.getQty())
            .remark(entity.getRemark())
            .documentNo(entity.getDocumentNo())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build())
        .toList();
  }
  // ✅ 구매 상세 조회

  @Override
  @Transactional(readOnly = true)
  public PurchaseOrderGetDetailResponse getPurchaseOrderById(final Long poId) {
    PurchaseOrder entity = purchaseOrderRepository.findById(poId)
        .orElseThrow(() -> new EntityNotFoundException("구매 전표를 조회할 수 없습니다. id=" + poId));

    return PurchaseOrderGetDetailResponse.builder()
        .poId(entity.getPoId())
        .roId(entity.getRequestOrder().getRoId())
        .qty(entity.getQty())
        .surtax(entity.getSurtax())
        .price(entity.getPrice())
        .remark(entity.getRemark())
        .documentNo(entity.getDocumentNo())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  // ✅ 구매 수정
  @Override
  @Transactional
  public PurchaseOrderUpdateResponse updatePurchaseOrder(final Long poId,
      final PurchaseOrderUpdateRequest request) {
    PurchaseOrder entity = purchaseOrderRepository.findById(poId)
        .orElseThrow(() -> new EntityNotFoundException("구매 전표를 조회할 수 없습니다. id=" + poId));

    // 엔티티 도메인 메서드 활용
    entity.updateQty(request.getQty());
    entity.updateSurtax(request.getSurtax());
    entity.updatePrice(request.getPrice());
    entity.updateRemark(request.getRemark());
    // documentNo 변경 요청이 있을 경우
    if (request.getNewDocDate() != null) {
      LocalDate newDate = request.getNewDocDate();
      int lastSeq = 0; // TODO: repository 로직 반영 예정
      entity.updateDocumentNo(newDate, lastSeq + 1);
    }

    return PurchaseOrderUpdateResponse.builder()
        .poId(entity.getPoId())
        .qty(entity.getQty())
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
        .orElseThrow(() -> new EntityNotFoundException("조회할 수 없습니다. id=" + poId));

    entity.delete(); // 엔티티 도메인 메서드: isDeleted=true, updatedAt=now

    return PurchaseOrderDeleteResponse.builder()
        .poId(entity.getPoId())
        .isDeleted(entity.isDeleted())
        .deletedAt(entity.getUpdatedAt())
        .message("구매 전표가 삭제되었습니다.")
        .build();
  }
}
