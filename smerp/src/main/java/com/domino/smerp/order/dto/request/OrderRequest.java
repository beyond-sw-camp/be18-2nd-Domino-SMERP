package com.domino.smerp.order.dto.request;

import com.domino.smerp.itemorder.dto.request.ItemOrderRequest;
import com.domino.smerp.order.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)   // ✅ Jackson 역직렬화 가능하게
public class OrderRequest {
    private final LocalDate orderDate;     // 주문일자
    private final Long clientId;           // 거래처 ID
    private final String empNo;         // 사번
    private final LocalDate deliveryDate;  // 납기일자
    private final String remark;           // 비고
    private final OrderStatus status;      // 상태값 (선택)
    private final List<ItemOrderRequest> items; // 품목 리스트
}