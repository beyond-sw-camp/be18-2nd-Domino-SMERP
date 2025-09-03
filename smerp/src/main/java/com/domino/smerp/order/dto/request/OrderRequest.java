package com.domino.smerp.order.dto.request;

import com.domino.smerp.order.constants.OrderStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class OrderRequest {
    private Long clientId;
    private Long userId;
    private OrderStatus status;   // ✅ Enum으로 수정
    private LocalDate deliveryDate;
    private String remark;
}
