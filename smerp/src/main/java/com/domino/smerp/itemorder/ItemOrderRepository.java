package com.domino.smerp.itemorder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemOrderRepository extends JpaRepository<com.domino.smerp.itemorder.ItemOrderCrossedTable, Long> {
}
