package com.domino.smerp.salesorder.repository;

import com.domino.smerp.salesorder.SalesOrder;
import com.domino.smerp.salesorder.repository.SalesOrderQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long>, SalesOrderQueryRepository {
    @Query(value = "SELECT MAX(CAST(SUBSTRING_INDEX(s.document_no, '-', -1) AS UNSIGNED)) " +
            "FROM sales_order s " +
            "WHERE s.document_no LIKE CONCAT(:prefix, '%')",
            nativeQuery = true)
    Optional<Integer> findMaxSequenceByPrefix(@Param("prefix") String prefix);
}
