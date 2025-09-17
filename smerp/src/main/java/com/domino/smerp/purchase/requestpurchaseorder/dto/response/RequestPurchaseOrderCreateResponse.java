package com.domino.smerp.purchase.requestpurchaseorder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class RequestPurchaseOrderCreateResponse {

    private final Long rpoId;            // 구매요청 PK
    private final Long userId;           // 작성자 FK
    private final LocalDate deliveryDate;// 납기요청일자
    private final String documentNo;     // 전표번호
    private final LocalDateTime createdAt; // 생성일시
}
