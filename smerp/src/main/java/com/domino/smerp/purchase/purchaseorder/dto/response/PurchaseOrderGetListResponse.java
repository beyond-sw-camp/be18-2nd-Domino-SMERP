package com.domino.smerp.purchase.purchaseorder.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.domino.smerp.client.Client;
import com.domino.smerp.purchase.purchaseorder.PurchaseOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PurchaseOrderGetListResponse {

    private final Long poId;

    private final String documentNo;

    private String empNo; // 사번

    private final String companyName;  // 거래처 회사명

    private final String warehouseName;

    private final BigDecimal qty;

    private final String remark;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant updatedAt;


    public static PurchaseOrderGetListResponse fromEntity(PurchaseOrder purchaseOrder) {
        return PurchaseOrderGetListResponse.builder()
                .poId(purchaseOrder.getPoId())
                .documentNo(purchaseOrder.getDocumentNo())  // 전표번호
                .empNo(purchaseOrder.getRequestOrder().getUser().getEmpNo()) // 담당자 사번
                .companyName(purchaseOrder.getRequestOrder().getClient().getCompanyName()) // 거래처명
                .warehouseName(purchaseOrder.getWarehouseName()) // 창고명
                .qty(purchaseOrder.getQty())   // 수량
                .remark(purchaseOrder.getRemark()) // 비고
                .createdAt(purchaseOrder.getCreatedAt()) // 생성일자
                .updatedAt(purchaseOrder.getUpdatedAt()) // 수정일자
                .build();
    }

}
