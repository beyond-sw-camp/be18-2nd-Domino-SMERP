package com.domino.smerp.purchase.requestorder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestOrderRepository extends JpaRepository<RequestOrder, Long> {

}
