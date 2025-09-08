package com.domino.smerp.salesorder.dto.request;

import com.domino.smerp.salesorder.constants.SalesOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)  // ✅ Jackson 역직렬화 지원
public class SalesOrderRequest {

    private final Long orderId;
    private final Integer qty;
    private final BigDecimal surtax;
    private final BigDecimal price;
    private final String remark;
    private final SalesOrderStatus status;   // ✅ Enum으로 변경
}
