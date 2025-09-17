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
public class RequestOrderCreateRequest {
    private final Long rpoId; // 선택적
    private final Long userId;
    private final Long clientId;
    private final Long itemId;
    private final BigDecimal qty;
    private final LocalDate deliveryDate;
    private final String remark;
    private final String documentNo; // 전표번호 (사용자 입력 or UI 선택 기반)
    // ✅ 다품목 입력
    private final List<ItemRequestOrderDto> items;
}
