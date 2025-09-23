package com.domino.smerp.purchase.requestpurchaseorder.dto.response;

import com.domino.smerp.purchase.itemrequestpurchaseorder.dto.request.ItemRequestPurchaseOrderDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RequestPurchaseOrderUpdateResponse {
    private final String documentNo;

    private String empNo; // 사번

    private final LocalDate deliveryDate;

    private final String remark;

    private final String status;

    private final List<ItemRequestPurchaseOrderDto> items;

//    private final BigDecimal specialPrice; // 요청 시 선택 입력

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant updatedAt;
}
