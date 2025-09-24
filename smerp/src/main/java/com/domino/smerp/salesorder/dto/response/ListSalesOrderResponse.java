package com.domino.smerp.salesorder.dto.response;

import com.domino.smerp.salesorder.SalesOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Builder
@AllArgsConstructor
public class ListSalesOrderResponse {

    private final Long soId;
    private final String documentNo;     // 판매 번호
    private final String companyName;    // 거래처
    private final String userName;       // 담당자
    private final String firstItemName;  // 첫 번째 품목
    private final int otherItemCount;    // 외 몇 건
    private final String warehouseName;  // 창고명
    private final BigDecimal totalAmount;// 금액 합계
    private final String orderDocumentNo;// 불러온 전표 (원 주문 번호)

    public static ListSalesOrderResponse from(SalesOrder so) {
        return ListSalesOrderResponse.builder()
                .soId(so.getSoId())
                .documentNo(so.getDocumentNo())
                .companyName(so.getOrder().getClient().getCompanyName())
                .userName(so.getOrder().getUser().getName())
                .firstItemName(so.getOrder().getFirstItemName())
                .otherItemCount(so.getOrder().getOtherItemCount())
                .warehouseName(so.getWarehouseName())
                .totalAmount(so.getOrder().getTotalAmount().setScale(2, RoundingMode.HALF_UP))
                .orderDocumentNo(so.getOrder().getDocumentNo())
                .build();
    }
}
