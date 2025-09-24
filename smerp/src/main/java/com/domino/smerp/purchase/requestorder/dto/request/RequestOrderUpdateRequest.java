package com.domino.smerp.purchase.requestorder.dto.request;

import com.domino.smerp.purchase.itemrequestorder.dto.request.ItemRequestOrderDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RequestOrderUpdateRequest {
    @NotNull(message = "납기일자는 필수 입력입니다.")
    private final LocalDate deliveryDate;

    @Size(max = 100, message = "비고는 최대 100자까지 입력 가능합니다.")
    private final String remark;

    @NotNull(message = "상태는 필수 입력입니다.")
    private final String status;     // 상태 (pending/approved/completed/returned)

    private final LocalDate newDocDate; // 전표 날짜 변경 (null이면 변경 없음)

    @NotNull(message = "주문 품목 리스트(items)는 필수 입력입니다.")
    @Size(min = 1, message = "최소 1개 이상의 품목이 필요합니다.")
    private final List<ItemRequestOrderDto> items;

    private final BigDecimal specialPrice; // 요청 시 선택 입력

}
