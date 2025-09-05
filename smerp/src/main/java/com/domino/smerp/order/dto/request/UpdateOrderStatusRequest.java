package com.domino.smerp.order.dto.request;

import com.domino.smerp.order.constants.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)  // ✅ Jackson 역직렬화 지원
public class UpdateOrderStatusRequest {
    private final OrderStatus status;   // ✅ Enum으로 변경
}
