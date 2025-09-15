package com.domino.smerp.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 다중 조회
    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.client " +
            "JOIN FETCH o.user " +
            "LEFT JOIN FETCH o.orderItems")
    List<Order> findAllWithDetails();

    // 단건 조회
    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.client " +
            "JOIN FETCH o.user " +
            "LEFT JOIN FETCH o.orderItems " +
            "WHERE o.orderId = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT MAX(CAST(SUBSTRING_INDEX(o.documentNo, '-', -1) AS int)) " +
            "FROM Order o " +
            "WHERE o.documentNo LIKE CONCAT(:prefix, '%')")
    Optional<Integer> findMaxSequenceByPrefix(@Param("prefix") String prefix);
}
