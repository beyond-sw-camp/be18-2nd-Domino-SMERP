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
public class OrderResponse {

    private final String documentNo;       // 주문 ID
    private final Long clientId;      // 거래처 ID
    private final String status;      // 주문 상태
    private final Instant deliveryDate; // 납기 일정
    private final String userName;        // 구매 담당자 ID
    private final Long itemId;        // 품목 ID
    private final BigDecimal totalAmount; // 공급 가격 (수량 * outbound_unit_price)
    private final String remark;      // 비고

    public static OrderResponse from(Order order) {
        // ✅ 첫 번째 품목 가져오기 (여러 개일 경우 대응 필요 시 확장 가능)
        ItemOrderCrossedTable firstItem = order.getOrderItems().stream()
                .findFirst()
                .orElse(null);

        // ✅ 총 공급가 계산 (qty × outbound_unit_price 합산)
        BigDecimal total = order.getOrderItems().stream()
                .map(itemOrder -> BigDecimal.valueOf(itemOrder.getQty())
                        .multiply(itemOrder.getItem().getOutboundUnitPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OrderResponse.builder()
                .documentNo(order.getDocumentNo())
                .clientId(order.getClient().getClientId())
                // 거래처 누락시 NPE 방지
                // client -> !=  null ? order.getClient().getCompanyName()
                .status(order.getStatus().name())
                .deliveryDate(order.getDeliveryDate())
                .userName(order.getUser().getName())
                .itemId(firstItem != null ? firstItem.getItem().getItemId() : null)
                .totalAmount(total)
                .remark(order.getRemark())
                .build();
    }
}
