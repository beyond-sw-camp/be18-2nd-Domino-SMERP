package com.domino.smerp.purchase.purchaseorder.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchPurchaseOrderRequest {
    private final Long poId;             // 구매전표 ID
    private final String documentNo;     // 전표번호
    private final String companyName;    // 거래처명
    private final String empNo;          // 담당자 사번
    private final String warehouseName;  // 창고명
}
