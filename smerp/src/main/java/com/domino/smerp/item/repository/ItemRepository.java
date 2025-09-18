package com.domino.smerp.item.repository;

import com.domino.smerp.item.Item;
import com.domino.smerp.item.repository.query.ItemQueryRepository;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemQueryRepository {

  boolean existsByItemId(final Long itemId);
  boolean existsByName(final String name);
  boolean existsByRfid(final String rfid);

  // 비관적 잠금용 메서드
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select i from Item i where i.itemId = :itemId")
  Optional<Item> findByIdWithPessimisticLock(final @Param("itemId") Long itemId);




  // TODO: 품목코드
  // boolean existsByItemCode(final String itemCode);
  // long countByItemCodeStartingWith(final String prefix);

}


