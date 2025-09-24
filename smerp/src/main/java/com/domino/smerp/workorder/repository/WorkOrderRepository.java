package com.domino.smerp.workorder.repository;

import com.domino.smerp.workorder.WorkOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long>, WorkOrderQueryRepository {

  List<WorkOrder> findByIsDeletedFalse();

  Optional<WorkOrder> findByIdAndIsDeletedFalse(Long id);

  @Query(value = "SELECT MAX(CAST(SUBSTRING_INDEX(w.document_no, '-', -1) AS UNSIGNED)) " +
      "FROM `work_order` w " +
      "WHERE w.document_no LIKE CONCAT(:prefix, '%')",
      nativeQuery = true)
  Optional<Integer> findMaxSequenceByPrefix(@Param("prefix") String prefix);

}
