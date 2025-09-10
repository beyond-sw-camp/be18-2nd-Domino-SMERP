package com.domino.smerp.item.repository;

import com.domino.smerp.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemQueryRepository {

  boolean existsByRfid(final String rfid);

}


