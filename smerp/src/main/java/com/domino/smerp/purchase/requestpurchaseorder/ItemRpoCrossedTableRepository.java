package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.ItemRpoCrossedTable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRpoCrossedTableRepository
    extends JpaRepository<ItemRpoCrossedTable, Long> {

  // 특정 RPO에 속한 모든 라인
  List<ItemRpoCrossedTable> findByRpo_Id(Long rpoId);

  // 특정 RPO 안에서 특정 품목 존재 여부 확인
  Optional<ItemRpoCrossedTable> findByRpo_IdAndItemId(Long rpoId, Long itemId);
}
