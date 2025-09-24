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
    private final String documentNo;     // 전표번호
    private final String companyName;    // 거래처명
    private final LocalDate deliveryDate;// 납기일자
    private final String userName;       // 영업 담당자
    private final String empNo;          // 사원 번호
    private final String remark;         // 비고
    private final String status;         // 주문 상태
    private final List<DetailItemOrderResponse> items;

    public static DetailOrderResponse from(Order order) {
        return DetailOrderResponse.builder()
                .documentNo(order.getDocumentNo())
                .companyName(order.getClient().getCompanyName())
                .deliveryDate(order.getDeliveryDate().atZone(ZoneOffset.UTC).toLocalDate())
                .empNo(order.getUser().getEmpNo())
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
