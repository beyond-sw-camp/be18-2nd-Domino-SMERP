package com.domino.smerp.item.repository;

import com.domino.smerp.item.entity.Item;
import com.domino.smerp.item.repository.query.ItemQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemQueryRepository {

  boolean existsById(final Long id);
  boolean existsByName(final String name);
  boolean existsByRfid(final String rfid);


  // TODO: 품목코드
  // boolean existsByItemCode(final String itemCode);
  long countByItemCodeStartingWith(final String prefix);

}


