package com.domino.smerp.salesorder;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesOrderRepository extends JpaRepository<com.domino.smerp.salesorder.SalesOrder, Long> {
}
