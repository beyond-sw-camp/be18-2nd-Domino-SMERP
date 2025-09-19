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
      AND (l.maxQty - l.curQty) >= :qty
    ORDER BY l.curQty ASC
  """)
  List<Location> findAvailableLocations(@Param("warehouseId") Long warehouseId,
      @Param("qty") BigDecimal qty);
}
