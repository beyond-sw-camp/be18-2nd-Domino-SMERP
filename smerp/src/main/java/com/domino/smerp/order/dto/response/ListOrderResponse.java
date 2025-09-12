package com.domino.smerp.order.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class ListOrderResponse {

    private final String documentNo;   // 전표번호
    private final String companyName;  // 거래처명
    private final String status;       // 주문 상태
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant deliveryDate;// 납기일
    private final String userName;     // 담당자 이름
    private final String firstItemName;// 첫 번째 품목 이름
    private final int otherItemCount;  // 나머지 품목 수
    private final BigDecimal totalAmount; // 총 공급가
    private final String remark;       // 비고
}

