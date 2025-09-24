package com.domino.smerp.purchase.requestpurchaseorder.dto.response;

import com.domino.smerp.purchase.itemrequestpurchaseorder.ItemRequestPurchaseOrder;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RequestPurchaseOrderGetListResponse {
    private final String documentNo;

    private final String empNo;     // 사번

    private final String itemName;

    private final BigDecimal totalQty;

    private final LocalDate deliveryDate;

    private final String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant createdAt;

    // ✅ 정적 팩토리 메서드 추가
    public static RequestPurchaseOrderGetListResponse fromEntity(RequestPurchaseOrder entity) {
        List<ItemRequestPurchaseOrder> items = entity.getItems();

        String itemName;
        BigDecimal totalQty;

        if (items.isEmpty()) {
            itemName = null;
            totalQty = BigDecimal.ZERO;
        } else if (items.size() == 1) {
            itemName = items.get(0).getItem().getName();
            totalQty = items.get(0).getQty();
        } else {
            itemName = items.get(0).getItem().getName() + " 외 " + (items.size() - 1) + "건";
            totalQty = items.stream()
                    .map(ItemRequestPurchaseOrder::getQty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return RequestPurchaseOrderGetListResponse.builder()
                .documentNo(entity.getDocumentNo())
                .empNo(entity.getUser().getEmpNo())
                .itemName(itemName)
                .totalQty(totalQty)
                .deliveryDate(entity.getDeliveryDate())
                .status(entity.getStatus().name())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}

