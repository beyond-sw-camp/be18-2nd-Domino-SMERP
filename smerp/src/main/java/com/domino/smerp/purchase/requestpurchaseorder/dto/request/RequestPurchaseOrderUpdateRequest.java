package com.domino.smerp.purchase.requestpurchaseorder.dto.request;

import com.domino.smerp.purchase.itemrequestpurchaseorder.dto.request.ItemRequestPurchaseOrderRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RequestPurchaseOrderUpdateRequest {
    private final LocalDate deliveryDate;      // 납기 요청일자
    private final String remark;               // 비고
    private final String status;               // 상태 변경 (PENDING, APPROVED, REJECTED)
    private final LocalDate newDocDate;        // 전표번호 변경용 날짜
    private final List<ItemRequestPurchaseOrderRequestDto> items; // 수정된 품목 리스트
}
