package com.domino.smerp.salesorder.dto.request;

import com.domino.smerp.salesorder.constants.SalesOrderStatus;
import lombok.Getter;

@Getter
public class SalesOrderRequest {
    private Long orderId;
    private Integer qty;
    private Double surtax;
    private Double price;
    private String remark;
    private SalesOrderStatus status;   // ✅ Enum으로 변경
}
