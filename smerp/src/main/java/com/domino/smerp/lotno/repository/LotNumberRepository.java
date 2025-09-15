package com.domino.smerp.lotno.repository;

import com.domino.smerp.lotno.entity.LotNumber;
import com.domino.smerp.lotno.repository.command.LotNumberCommandRepository;
import com.domino.smerp.lotno.repository.query.LotNumberQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotNumberRepository extends JpaRepository<LotNumber, Long>,
    LotNumberQueryRepository, LotNumberCommandRepository {

  boolean existsByLotId(final Long lotId);

  boolean existsByName(final String name);

  long countByNameStartingWith(final String lotNumberPrefix);

}
