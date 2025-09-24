package com.domino.smerp.purchase.requestpurchaseorder.repository;

import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import com.domino.smerp.purchase.requestpurchaseorder.repository.query.RequestPurchaseOrderQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestPurchaseOrderRepository extends JpaRepository<RequestPurchaseOrder, Long>, RequestPurchaseOrderQueryRepository {

    // ✅ 상세 조회 (추후 필요 시 fetch join으로 확장 가능)
    @Query("SELECT rpo FROM RequestPurchaseOrder rpo WHERE rpo.rpoId = :rpoId")
    Optional<RequestPurchaseOrder> findByIdWithDetails(@Param("rpoId") Long rpoId);

    // ✅ 전표번호 수정용 시퀀스 조회 (yyyy/MM/dd-n 형식)
    @Query("SELECT MAX(CAST(SUBSTRING(rpo.documentNo, 12) AS int)) " +
            "FROM RequestPurchaseOrder rpo " +
            "WHERE SUBSTRING(rpo.documentNo, 1, 10) = :dateString")
    Optional<Integer> findLastSequenceByDate(@Param("dateString") String dateString);
}
