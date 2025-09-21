package com.domino.smerp.productionplan;

import com.domino.smerp.productionplan.constants.Status;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionPlanRepository extends JpaRepository<ProductionPlan, Long>{

  List<ProductionPlan> findByIsDeletedFalse();

  Optional<ProductionPlan> findByIdAndIsDeletedFalse(Long id);

  boolean existsByTitleAndIsDeletedFalse(String title);

  boolean existsByTitle(String title);
/*
  @Query("SELECT p FROM ProductionPlan p" +
      "JOIN p.itemOrder io" +
      "JOIN io.item i" +
      "WHERE p.status = :status AND i.id = :itemId")
  List<ProductionPlan> findByStatusAndItemId(
      @Param("status") Status status,
      @Param("itemId") Long itemId
  );
*/
}
