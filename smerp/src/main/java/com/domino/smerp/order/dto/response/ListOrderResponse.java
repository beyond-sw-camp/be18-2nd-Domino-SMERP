package com.domino.smerp.order.dto.response;

import com.domino.smerp.itemorder.ItemOrderCrossedTable;
import com.domino.smerp.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class ListOrderResponse {

    private final String documentNo;       // 주문 ID
    private final String companyName;      // 거래처 ID
    private final String status;      // 주문 상태
    private final Instant deliveryDate; // 납기 일정
    private final String userName;        // 구매 담당자 ID
    private final Long itemId;        // 품목 ID
    private final BigDecimal totalAmount; // 공급 가격 (수량 * outbound_unit_price)
    private final String remark;      // 비고

    public static ListOrderResponse from(Order order) {
        ItemOrderCrossedTable firstItem = order.getFirstItem();

        return ListOrderResponse.builder()
                .documentNo(order.getDocumentNo())
                .companyName(order.getClient().getCompanyName())
                // 거래처 누락시 NPE 방지
                // client -> !=  null ? order.getClient().getCompanyName()
                .status(order.getStatus().name())
                .deliveryDate(order.getDeliveryDate())
                .userName(order.getUser().getName())
                .itemId(firstItem != null ? firstItem.getItem().getItemId() : null)
                .totalAmount(order.getTotalAmount())
                .remark(order.getRemark())
                .build();
    }
}
