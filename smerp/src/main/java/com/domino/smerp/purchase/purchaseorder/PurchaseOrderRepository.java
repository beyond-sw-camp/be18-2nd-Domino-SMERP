package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderGetListResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

  // ✅ 상세 조회: fetch join
  @Query("select purchaseOrder from PurchaseOrder purchaseOrder " +
      "join fetch purchaseOrder.requestOrder " +
      "where purchaseOrder.poId = :poId")
  Optional<PurchaseOrder> findByIdWithRequestOrder(@Param("poId") final Long poId);

//  // ✅ 목록 조회: Projection 방식으로 DTO 반환
//  @Query("select new com.domino.smerp.purchase.purchaseorder.dto.response.PurchaseOrderGetListResponse( " +
//      "purchaseOrder.poId, purchaseOrder.qty, purchaseOrder.remark, " +
//      "purchaseOrder.documentNo, purchaseOrder.createdAt, purchaseOrder.updatedAt) " +
//      "from PurchaseOrder purchaseOrder")
//  Page<PurchaseOrderGetListResponse> findAllList(final Pageable pageable);
}
