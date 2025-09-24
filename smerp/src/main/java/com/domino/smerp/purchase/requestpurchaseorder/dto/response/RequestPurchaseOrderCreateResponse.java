package com.domino.smerp.purchase.requestpurchaseorder.dto.response;

import com.domino.smerp.purchase.requestpurchaseorder.constants.RequestPurchaseOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RequestPurchaseOrderCreateResponse {
    private final String documentNo;   // 전표 번호

    private String empNo; // 사번

    private final RequestPurchaseOrderStatus status; // 상태

    private final String message;      // 처리 메시지

    private final List<ItemDetail> items; // 요청 품목 리스트

    private String remark; // 비고

    @Getter
    @AllArgsConstructor
    public static class ItemDetail {
        private final Long itemId;
        private final BigDecimal qty;
        private final BigDecimal inboundUnitPrice;
        private final BigDecimal specialPrice; // 요청 시 선택 입력
    }
}

