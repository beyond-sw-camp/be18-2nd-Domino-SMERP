package com.domino.smerp.location;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

  List<Location> findAllByWarehouseIdAndFilledFalse(Long warehouseId);

  @Query("""
    SELECT l 
    FROM Location l
    WHERE l.warehouse.id = :warehouseId
      AND COALESCE(l.curQty, 0) < l.maxQty
    ORDER BY l.curQty ASC
  """)
  List<Location> findAvailableLocationsWithCurQty(@Param("warehouseId") Long warehouseId,
      @Param("qty") BigDecimal qty);
}
