package com.domino.smerp.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

  boolean existsByRfid(String rfid);


    /* 추후 구현 예정
    // 품목명으로 조회
    List<Item> findByName(String name);

    // 특정 상태값으로 조회
    List<Item> findByItemAct(ItemAct itemAct);

    // 안전재고 미만인 품목 조회
    List<Item> findBySafetyStockLessThan(Integer stock);

    // 특정 그룹명으로 조회
    List<Item> findByGroupName1AndGroupName2AndGroupName3(String groupName1, String groupName2, String groupName3);

    */

}


