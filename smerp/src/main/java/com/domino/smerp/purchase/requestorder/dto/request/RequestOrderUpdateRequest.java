package com.domino.smerp.purchase.requestorder.dto.request;

import com.domino.smerp.purchase.itemrequestorder.dto.request.ItemRequestOrderDto;
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
    private final BigDecimal qty;
    private final LocalDate deliveryDate;
    private final String remark;
    private final String status;     // 상태 (pending/approved/completed/returned)
    private final LocalDate newDocDate; // 전표 날짜 변경 (null이면 변경 없음)
    // ✅ 기존 items도 교체/추가/삭제 가능
    private final List<ItemRequestOrderDto> items;
}
