// dto/request/UpdateOrderStatusRequest.java
package com.domino.smerp.order.dto.request;

import com.domino.smerp.order.constants.OrderStatus;
import lombok.Getter;

@Getter
public class UpdateOrderStatusRequest {
    private OrderStatus status;   // ✅ String → Enum으로 변경
}
