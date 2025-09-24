package com.domino.smerp.productionresult;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionResultRepository extends JpaRepository<ProductionResult, Long> {


  List<ProductionResult> findByIsDeletedFalse();

  Optional<ProductionResult> findByIdAndIsDeletedFalse(Long id);
}
