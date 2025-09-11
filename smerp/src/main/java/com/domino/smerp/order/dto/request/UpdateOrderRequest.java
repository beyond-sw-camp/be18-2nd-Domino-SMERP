package com.domino.smerp.order.dto.request;

import com.domino.smerp.itemorder.dto.request.ItemOrderRequest;
import com.domino.smerp.order.constants.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true) // Jackson 역직렬화 가능
public class UpdateOrderRequest {

    @NotBlank(message = "사번(empNo)은 필수 입력입니다.")
    private final String empNo;       // 담당자 사번

    @NotNull(message = "주문일자(documentDate)는 필수 입력입니다.")
    private final LocalDate documentDate;     // 주문일자

    @NotNull(message = "납기일자(deliveryDate)는 필수 입력입니다.")
    private final LocalDate deliveryDate;     // 납기일자

    @Size(max = 100, message = "비고는 최대 100자까지 입력 가능합니다.")
    private final String remark;              // 비고

    @NotNull(message = "상태(status)는 필수 입력입니다.")
    private final OrderStatus status;         // 주문 상태

    @NotNull(message = "품목 리스트(items)는 필수 입력입니다.")
    @Size(min = 1, message = "최소 1개 이상의 품목이 필요합니다.")
    private final List<ItemOrderRequest> items; // 품목 리스트
}
