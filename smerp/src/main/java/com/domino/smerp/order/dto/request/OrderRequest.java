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
    private final Long clientId;
    private final Long userId;
    private final OrderStatus status;
    private final LocalDate deliveryDate;
    private final String remark;
    private final List<ItemOrderRequest> items;
}
