package com.domino.smerp.order.dto.response;

import com.domino.smerp.itemorder.dto.response.DetailItemOrderResponse;
import com.domino.smerp.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class DetailOrderResponse {
    private String documentNo;     // 전표번호
    private String companyName;    // 거래처명
    private LocalDate deliveryDate;// 납기일자
    private String userName;       // 영업 담당자
    private String remark;         // 비고
    private String status;         // 주문 상태
    private List<DetailItemOrderResponse> items;

    public static DetailOrderResponse from(Order order) {
        return DetailOrderResponse.builder()
                .documentNo(order.getDocumentNo())
                .companyName(order.getClient().getCompanyName())
                .deliveryDate(order.getDeliveryDate().atZone(ZoneOffset.UTC).toLocalDate())
                .userName(order.getUser().getName())
                .remark(order.getRemark())
                .status(order.getStatus().name())
                .items(
                        order.getOrderItems().stream()
                                .map(DetailItemOrderResponse::from)
                                .toList()
                )
                .build();
    }
}
