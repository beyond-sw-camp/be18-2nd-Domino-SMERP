package com.domino.smerp.stock;

import com.domino.smerp.warehouse.Warehouse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

  //각 품목별 안전재고 수량을 파악하고 총 재고가 이를 넘는지 확인
  @Query("SELECT SUM(s.qty) FROM Stock s WHERE s.item.itemId = :itemId")
  BigDecimal sumQuantityByItemId(@Param("itemId") Long itemId);

  Optional<Stock> findByItemItemId(Long itemId);

  boolean existsByItemItemId(Long itemId);

  @Query("""
    SELECT s
    FROM Stock s
    WHERE s.item.itemId = :itemId
      AND s.location.warehouse.id = :warehouseId
      AND s.qty > 0
    ORDER BY s.location.curQty ASC
  """)
  List<Stock> findByItemIdAndWarehouseId(
      @Param("itemId") Long itemId,
      @Param("warehouseId") Long warehouseId);

}
