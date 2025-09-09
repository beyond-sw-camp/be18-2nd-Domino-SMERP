package com.domino.smerp.warehouse;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

  Boolean existsByName(String name);

  @Query("SELECT DISTINCT w.id FROM Warehouse w " +
      "WHERE NOT EXISTS (" +
      "  SELECT l FROM Location l " +
      "  WHERE l.warehouse = w AND l.filled = true" +
      ")")
  List<Warehouse> findWarehousesWithFilledFalseLocations();
}
