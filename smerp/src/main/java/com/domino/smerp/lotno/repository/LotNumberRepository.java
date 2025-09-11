package com.domino.smerp.lotno.repository;

import com.domino.smerp.lotno.entity.LotNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotNumberRepository extends JpaRepository<LotNumber, Long> {

}
