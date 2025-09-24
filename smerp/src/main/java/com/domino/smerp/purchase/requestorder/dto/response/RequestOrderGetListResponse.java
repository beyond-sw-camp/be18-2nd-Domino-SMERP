package com.domino.smerp.purchase.requestorder.dto.response;

import com.domino.smerp.purchase.itemrequestorder.ItemRequestOrder;
import com.domino.smerp.purchase.requestorder.RequestOrder;
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
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RequestOrderGetListResponse {

    private final Long roId;

    private final String documentNo;

    private String empNo; // 사번

    private final String companyName;  // 거래처 회사명

    private final String itemName;   // 품목명 or "XXX 외 n건"

    private final BigDecimal totalQty; // 품목 수량 총합

    private final LocalDate deliveryDate;

    private final String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private final Instant createdAt;

    public static RequestOrderGetListResponse fromEntity(RequestOrder entity) {
        List<ItemRequestOrder> items = entity.getItems();

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
                    .map(ItemRequestOrder::getQty)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        return RequestOrderGetListResponse.builder()
                .roId(entity.getRoId())
                .empNo(entity.getUser().getEmpNo())
                .companyName(entity.getClient().getCompanyName())
                .itemName(itemName)
                .totalQty(totalQty)
                .deliveryDate(entity.getDeliveryDate())
                .status(entity.getStatus().name())
                .documentNo(entity.getDocumentNo())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
