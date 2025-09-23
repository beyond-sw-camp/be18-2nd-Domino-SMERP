package com.domino.smerp.item.repository;

import com.domino.smerp.item.Item;
import com.domino.smerp.item.ItemStatus;
import com.domino.smerp.item.constants.ItemAct;
import com.domino.smerp.item.repository.query.ItemQueryRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemQueryRepository {

  boolean existsByItemId(final Long itemId);
  boolean existsByName(final String name);
  boolean existsByRfid(final String rfid);

  // 품목명 검색
  Optional<Item> findByName(final String name);
  // RFID 검색
  Optional<Item> findByRfid(final String rfid);
  // 품목구분 검색
  List<Item> findByItemStatus(final ItemStatus itemStatus);
  // 품목 사용상태
  List<Item> findByItemAct(final ItemAct itemAct); //(ACTIVE("사용중"),INACTIVE("사용중지")

  // 안전재고 수량
  List<Item> findBySafetyStockLessThan(final BigDecimal value);   // 안전재고 미만인 품목들
  List<Item> findBySafetyStockGreaterThan(final BigDecimal value); // 안전재고 이상인 품목들


  // TODO: 품목코드
  // boolean existsByItemCode(final String itemCode);
  // long countByItemCodeStartingWith(final String prefix);

}


