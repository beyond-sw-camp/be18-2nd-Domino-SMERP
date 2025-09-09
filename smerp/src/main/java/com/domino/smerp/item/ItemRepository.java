package com.domino.smerp.item;

import com.domino.smerp.item.constants.ItemAct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> , JpaSpecificationExecutor<Item> {

  boolean existsByRfid(final String rfid);

  /*
  // 품목명 검색 (부분일치, 대소문자 무시)
  List<Item> findByNameContainingIgnoreCase(final String name);

  // 품목 사용중/사용중지로 조회
  List<Item> findByItemAct(final ItemAct itemAct);

  // 안전재고 미만인 품목 조회
  List<Item> findBySafetyStockLessThan(final Integer safetyStock);

  // 특정 그룹명으로 조회
  List<Item> findByGroupName1AndGroupName2AndGroupName3(final String groupName1, final String groupName2, final String groupName3);
  */

}


