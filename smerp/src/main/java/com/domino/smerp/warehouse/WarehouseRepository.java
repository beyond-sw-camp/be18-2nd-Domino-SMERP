package com.domino.smerp.warehouse;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

  Boolean existsByName(String name);

//  Optional<Warehouse> findByItemId(Long itemId);

  @Query("SELECT DISTINCT w.id FROM Warehouse w " +
      "WHERE NOT EXISTS (" +
      "  SELECT l FROM Location l " +
      "  WHERE l.warehouse = w AND l.filled = true" +
      ")")
  List<Warehouse> findWarehousesWithFilledFalseLocations();

  @Query("""
    SELECT w
    FROM Warehouse w
    WHERE EXISTS (
        SELECT 1
        FROM Location l
        WHERE l.warehouse = w
          AND COALESCE(l.curQty, 0) < l.maxQty
    )
  """)
  List<Warehouse> findAvailableWarehousesWithCurQty();

//  @Query("""
//    SELECT DISTINCT s.location.warehouse
//    FROM Stock s
//    WHERE s.item.itemId = :itemId
//      AND s.qty > 0
//  """)
//  List<Warehouse> findWarehousesWithStock(@Param("itemId") Long itemId);

  Optional<Warehouse> findByName(String name);

}

