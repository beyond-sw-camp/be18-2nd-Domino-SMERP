package com.domino.smerp.purchase.itemrequestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestPurchaseOrderRepository extends JpaRepository<ItemRequestPurchaseOrder, Long> {

    // ✅ 특정 구매요청(rpoId)에 속한 품목 리스트 조회
    List<ItemRequestPurchaseOrder> findByRequestPurchaseOrder_RpoId(Long rpoId);

    // ✅ 특정 구매요청 엔티티로 품목 리스트 조회
    List<ItemRequestPurchaseOrder> findByRequestPurchaseOrder(RequestPurchaseOrder requestPurchaseOrder);

    // ✅ 특정 품목(itemId)으로 조회
    List<ItemRequestPurchaseOrder> findByItem_ItemId(Long itemId);

    // ✅ 특정 구매요청과 연관된 품목 전체 삭제 (구매요청 수정 시 기존 품목 제거용)
    void deleteByRequestPurchaseOrder(RequestPurchaseOrder requestPurchaseOrder);

    // ✅ 특정 품목 name으로 조회
    List<ItemRequestPurchaseOrder> findByItem_Name(String name);
}
