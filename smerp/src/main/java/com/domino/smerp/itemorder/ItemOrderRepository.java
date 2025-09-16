package com.domino.smerp.itemorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemOrderRepository extends JpaRepository<ItemOrder, Long> {
    List<ItemOrder> findByOrder_OrderId(Long orderId);
}
