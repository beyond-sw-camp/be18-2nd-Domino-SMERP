// file: src/main/java/com/domino/smerp/purchase/requestorder/dto/request/SearchRequestOrderRequest.java
package com.domino.smerp.purchase.requestorder.dto.request;

import com.domino.smerp.purchase.requestorder.constants.RequestOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchRequestOrderRequest {
    private final Long roId;           // 발주전표 ID
    private final String documentNo;   // 전표번호
    private final String companyName;  // 거래처명
    private final String empNo;        // 담당자 사번
    private final RequestOrderStatus status;       // 발주 상태
    private final String remark;       // 비고 검색
}
