package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderCreateRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.PurchaseOrderUpdateRequest;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderCreateResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderDeleteResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderGetDetailResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderGetListResponse;
import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderUpdateResponse;
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

  // ✅ 구매 생성
  @Override
  @Transactional
  public PurchaseOrderCreateResponse createPurchaseOrder(final PurchaseOrderCreateRequest request) {
    // TODO: RequestOrder 엔티티 생성 후 request.toEntity(requestOrder) 로 변환
    throw new UnsupportedOperationException("RequestOrder 엔티티 필요");
  }

  // ✅ 구매 수정
  @Override
  @Transactional
  public PurchaseOrderUpdateResponse updatePurchaseOrder(final Long poId,
      final PurchaseOrderUpdateRequest request) {
    PurchaseOrder entity = purchaseOrderRepository.findById(poId)
        .orElseThrow(() -> new EntityNotFoundException("조회할 수 없습니다. id=" + poId));

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
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  // ✅ 구매 목록 조회 (페이징)
  @Override
  @Transactional(readOnly = true)
  public List<PurchaseOrderGetListResponse> getPurchaseOrders(final int page, final int size) {
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
        .orElseThrow(() -> new EntityNotFoundException("구매 전표를 찾을 수 없습니다. id=" + poId));

    return PurchaseOrderGetDetailResponse.builder()
        .poId(entity.getPoId())
//        .roId(entity.getRequestOrder().getRoId())
        .qty(entity.getQty())
        .surtax(entity.getSurtax())
        .price(entity.getPrice())
        .remark(entity.getRemark())
        .documentNo(entity.getDocumentNo())
        .isDeleted(entity.isDeleted())
        .createdAt(entity.getCreatedAt())
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
