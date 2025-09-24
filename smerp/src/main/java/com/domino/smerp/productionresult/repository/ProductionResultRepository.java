package com.domino.smerp.productionresult;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionResultRepository extends JpaRepository<ProductionResult, Long> {


  List<ProductionResult> findByIsDeletedFalse();

  Optional<ProductionResult> findByIdAndIsDeletedFalse(Long id);

  @Query(value = "SELECT MAX(CAST(SUBSTRING_INDEX(p.document_no, '-', -1) AS UNSIGNED)) " +
      "FROM `production_result` p " +
      "WHERE p.document_no LIKE CONCAT(:prefix, '%')",
      nativeQuery = true)
  Optional<Integer> findMaxSequenceByPrefix(@Param("prefix") String prefix);

}
