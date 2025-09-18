package com.domino.smerp.order.repository;

import com.domino.smerp.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderQueryRepository {

    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.client " +
            "JOIN FETCH o.user " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.item " +   // Item 까지 fetch join
            "WHERE o.orderId = :id")

    Optional<Order> findByIdWithDetails(@Param("id") Long id);

    // 전표 번호 기반 조회
    Optional<Order> findByDocumentNo(String documentNo);

    @Query(value = "SELECT MAX(CAST(SUBSTRING_INDEX(o.document_no, '-', -1) AS UNSIGNED)) " +
            "FROM `order` o " +
            "WHERE o.document_no LIKE CONCAT(:prefix, '%')",
            nativeQuery = true)
    Optional<Integer> findMaxSequenceByPrefix(@Param("prefix") String prefix);

    List<Order> findByDocumentNoStartingWith(String prefix);
}

