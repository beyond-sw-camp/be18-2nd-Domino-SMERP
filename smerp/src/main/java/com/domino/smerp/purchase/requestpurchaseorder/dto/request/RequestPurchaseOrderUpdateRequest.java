package com.domino.smerp.purchase.requestpurchaseorder.dto.request;

import com.domino.smerp.purchase.itemrequestpurchaseorder.dto.request.ItemRequestPurchaseOrderDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RequestPurchaseOrderUpdateRequest {
    @NotNull(message = "일자는 필수 입력입니다.")
    private final LocalDate newDocDate;        // 전표번호 변경용 날짜

    @NotNull(message = "납기일자는 필수 입력입니다.")
    private final LocalDate deliveryDate;      // 납기 요청일자

    @Size(max = 100, message = "비고는 최대 100자까지 입력 가능합니다.")
    private final String remark;               // 비고

    @NotNull(message = "상태(status)는 필수 입력입니다.")
    private final String status;               // 상태 변경 (PENDING, APPROVED, REJECTED)

    @NotNull(message = "품목 리스트(items)는 필수 입력입니다.")
    @Size(min = 1, message = "최소 1개 이상의 품목이 필요합니다.")
    private final List<ItemRequestPurchaseOrderDto> items; // 수정된 품목 리스트
}
