package com.domino.smerp.salesorder.dto.response;

import com.domino.smerp.itemorder.dto.response.DetailItemOrderResponse;
import com.domino.smerp.salesorder.SalesOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class DetailSalesOrderResponse {

    private final String documentNo;       // 판매번호
    private final String companyName;      // 거래처
    private final String userName;         // 담당자
    private final String remark;           // 비고
    private final String warehouseName;    // 창고명
    private final BigDecimal totalAmount;  // 금액 합계
    private final List<DetailItemOrderResponse> items; // 품목 목록

    public static DetailSalesOrderResponse from(SalesOrder so) {
        return DetailSalesOrderResponse.builder()
                .documentNo(so.getDocumentNo())
                .companyName(so.getOrder().getClient().getCompanyName())
                .userName(so.getOrder().getUser().getName())
                .remark(so.getRemark())
                .warehouseName(so.getWarehouseName())
                .totalAmount(so.getOrder().getTotalAmount().setScale(2, RoundingMode.HALF_UP))
                .items(
                        so.getOrder().getOrderItems().stream()
                                .map(DetailItemOrderResponse::from)
                                .toList()
                )
                .build();
    }
}
