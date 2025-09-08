package com.domino.smerp.purchase.itemrocrossedtable;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRoCrossedTableRepository extends JpaRepository<ItemRoCrossedTable, Long> {

  // 특정 발주 전표에 속한 라인 조회
  List<ItemRoCrossedTable> findByRequestOrder_RoId(Long roId);

  // 발주 안에서 특정 품목 존재 여부 확인
  Optional<ItemRoCrossedTable> findByRequestOrder_RoIdAndItem_ItemId(Long roId, Long itemId);
}
