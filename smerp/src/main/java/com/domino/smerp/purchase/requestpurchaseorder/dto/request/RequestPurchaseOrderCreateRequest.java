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
public class RequestPurchaseOrderCreateRequest {
    private final Long userId;                 // 작성자 FK
    private final LocalDate deliveryDate;      // 납기 요청일자
    private final String remark;               // 비고
    private final String documentNo;           // 전표번호
    private final List<ItemRequestPurchaseOrderRequestDto> items;}
