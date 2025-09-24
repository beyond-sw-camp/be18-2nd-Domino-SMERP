package com.domino.smerp.purchase.requestorder.repository;

import com.domino.smerp.purchase.requestorder.RequestOrder;
import com.domino.smerp.purchase.requestorder.repository.query.RequestOrderQueryRepository;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestOrderRepository extends JpaRepository<RequestOrder, Long>, RequestOrderQueryRepository {

    // ✅ 상세 조회 시 fetch join (추후 필요한 경우)
    @Query("select ro from RequestOrder ro left join fetch ro.purchaseOrder where ro.roId = :roId")
    Optional<RequestOrder> findByIdWithPurchaseOrder(@Param("roId") Long roId);

    // ✅ 전표번호 수정용 시퀀스 조회
    @Query("SELECT MAX(CAST(SUBSTRING(ro.documentNo, 12) AS int)) " +
            "FROM RequestOrder ro " +
            "WHERE SUBSTRING(ro.documentNo, 1, 10) = :dateString")
    Optional<Integer> findLastSequenceByDate(@Param("dateString") String dateString);


    @Query("SELECT r FROM RequestPurchaseOrder r WHERE r.status = 'PENDING'")
    List<RequestPurchaseOrder> findAllPending();

}
