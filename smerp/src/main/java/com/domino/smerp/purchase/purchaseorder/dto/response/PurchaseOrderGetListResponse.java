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
                .documentNo(purchaseOrder.getDocumentNo())
                .warehouseName(purchaseOrder.getWarehouseName())
                .qty(purchaseOrder.getQty())
                .remark(purchaseOrder.getRemark())
                .build();
    }

}
