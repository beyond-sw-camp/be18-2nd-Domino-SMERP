package com.domino.smerp.order.dto.request;

import com.domino.smerp.itemorder.dto.request.ItemOrderRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CreateReturnOrderRequest {

    @NotBlank(message = "반품하려는 전표번호는 필수 입력입니다.")
    private final String documentNo;

    @NotBlank(message = "반품 사유는 필수 입력입니다.")
    private final String remark;

    @NotBlank(message = "사번은 필수 입력입니다.")
    private final String empNo;

    @NotNull(message = "반품 품목 리스트는 필수 입력입니다.")
    @Size(min = 1, message = "최소 1개 이상의 품목이 필요합니다.")
    private final List<ItemOrderRequest> items;
}
