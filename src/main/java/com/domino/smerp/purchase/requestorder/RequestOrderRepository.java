package com.domino.smerp.purchase.requestorder;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestOrderRepository extends JpaRepository<RequestOrder, Long> {

  // 상태별 조회
  List<RequestOrder> findByStatus(RequestOrderStatus status);

  // 특정 사용자 기준 조회
  List<RequestOrder> findByUser_UserId(Long userId);

  // 특정 거래처 기준 조회
  List<RequestOrder> findByClient_ClientId(Long clientId);
}
