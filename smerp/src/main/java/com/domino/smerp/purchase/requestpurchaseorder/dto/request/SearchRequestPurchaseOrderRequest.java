package com.domino.smerp.purchase.requestpurchaseorder.dto.request;

import com.domino.smerp.purchase.requestpurchaseorder.constants.RequestPurchaseOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchRequestPurchaseOrderRequest {
    private final Long rpoId;                      // 구매요청 ID
    private final String documentNo;               // 전표번호
    private final String companyName;              // 거래처명
    private final String empNo;                    // 담당자 사번
    private final RequestPurchaseOrderStatus status; // 상태 (enum)
    private final String remark;                   // 비고 검색
}
