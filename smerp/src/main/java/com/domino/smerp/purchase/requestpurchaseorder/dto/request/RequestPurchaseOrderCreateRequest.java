package com.domino.smerp.purchase.requestpurchaseorder.dto.request;

import com.domino.smerp.purchase.itemrequestpurchaseorder.dto.request.ItemRequestPurchaseOrderDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RequestPurchaseOrderCreateRequest {
    @NotNull(message = "작성자 ID는 필수 입력입니다.")
    private String empNo; // 작성자 FK

    @NotNull(message = "납기일자는 필수 입력입니다.")
    private LocalDate deliveryDate; // 납기일자

    @Size(max = 100, message = "비고는 최대 100자까지 입력 가능합니다.")
    private String remark; // 비고

    @NotBlank(message = "전표번호는 필수 입력입니다.")
    private String documentNo; // 전표번호

    @NotNull(message = "품목 리스트(items)는 필수 입력입니다.")
    @Size(min = 1, message = "최소 1개 이상의 품목이 필요합니다.")
    private List<ItemRequestPurchaseOrderDto> items; // 품목 리스트
}
