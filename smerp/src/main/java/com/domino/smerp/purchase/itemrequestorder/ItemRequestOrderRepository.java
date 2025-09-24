package com.domino.smerp.purchase.itemrequestorder;

import com.domino.smerp.purchase.requestorder.RequestOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRequestOrderRepository extends JpaRepository<ItemRequestOrder, Long> {
    // ✅ 특정 발주(roId)에 속한 품목 리스트 조회
    List<ItemRequestOrder> findByRequestOrder_RoId(Long roId);

    // ✅ 특정 발주 엔티티로 품목 리스트 조회
    List<ItemRequestOrder> findByRequestOrder(RequestOrder requestOrder);

    // ✅ 특정 품목(itemId)으로 조회
    List<ItemRequestOrder> findByItem_ItemId(Long itemId);

    // ✅ 특정 발주와 연관된 품목 전체 삭제 (발주 수정 시 기존 품목 제거용)
    void deleteByRequestOrder(RequestOrder requestOrder);

    // ✅ 특정 품목 name으로 조회
    List<ItemRequestOrder> findByItem_Name(String name);
}

