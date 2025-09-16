package com.domino.smerp.itemorder.dto.response;

import com.domino.smerp.itemorder.ItemOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class DetailItemOrderResponse {
    private Long itemId;       // 품목코드1
    private String itemName;       // 품목명
    private String specification;  // 규격
    private BigDecimal qty;        // 수량
    private String unit;           // 단위
    private BigDecimal specialPrice;  // 단가
    private BigDecimal supplyAmount; // 공급가액
    private BigDecimal tax;        // 부가세
    private BigDecimal totalAmount; // 금액
    private LocalDate deliveryDate; // 납기일자
    private String remark;           // 적요

    public static DetailItemOrderResponse from(ItemOrder itemOrder) {
        return DetailItemOrderResponse.builder()
                .itemId(itemOrder.getItem().getItemId())
                .itemName(itemOrder.getItem().getName())
                .specification(itemOrder.getItem().getSpecification())
                .qty(itemOrder.getQty())
                .unit(itemOrder.getItem().getUnit())
                .specialPrice(itemOrder.getSpecialPrice())
                .supplyAmount(itemOrder.getSupplyAmount())
                .tax(itemOrder.getTax())
                .totalAmount(itemOrder.getTotalAmount())
                .deliveryDate(itemOrder.getOrder().getDeliveryDate()
                        .atZone(java.time.ZoneOffset.UTC).toLocalDate())
                .remark(itemOrder.getOrder().getRemark())
                .build();
    }

}
