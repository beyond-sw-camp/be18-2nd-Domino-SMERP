package com.domino.smerp.workorder.repository;

import com.domino.smerp.workorder.WorkOrder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long>, WorkOrderQueryRepository {

  List<WorkOrder> findByIsDeletedFalse();

  Optional<WorkOrder> findByIdAndIsDeletedFalse(Long id);
}
