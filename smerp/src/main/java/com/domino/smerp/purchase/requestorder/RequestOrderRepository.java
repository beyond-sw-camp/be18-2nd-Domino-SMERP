package com.domino.smerp.purchase.requestorder;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestOrderRepository extends JpaRepository<RequestOrder, Long> {

  // created_date 조회
  List<RequestOrder> findByCreatedDateBetween(Instant start, Instant end);

  // updated_date 조회
  List<RequestOrder> findByUpdatedDateBetween(Instant start, Instant end);

  // delivery_date 조회
  List<RequestOrder> findByDeliveryDateBetween(Instant start, Instant end);

  // user_id 조회
  List<RequestOrder> findByUserUserId(Long userId);

  // item_id 조회 (교차테이블 기반)
  List<RequestOrder> findByItemRoCrossedTablesItemItemId(Long itemId);

  // client_id 조회
  List<RequestOrder> findByClientClientId(Long clientId);

  // qty 조회 (교차테이블 기반)
  List<RequestOrder> findByItemRoCrossedTablesQty(int qty);

  // remark 조회
  List<RequestOrder> findByRemarkContaining(String keyword);
}
