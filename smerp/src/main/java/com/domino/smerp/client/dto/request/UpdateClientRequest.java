package com.domino.smerp.client.dto.request;

import com.domino.smerp.client.constants.TradeType;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdateClientRequest {

    private final String companyName;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "대표 전화번호 형식이 올바르지 않습니다.")
    private final String phone;

    private final String ceoName;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "대표자 휴대폰 형식이 올바르지 않습니다.")
    private final String ceoPhone;

    private final String name1st;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "1차 담당자 휴대폰 형식이 올바르지 않습니다.")
    private final String phone1st;

    private final String job1st;

    private final String name2nd;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "2차 담당자 휴대폰 형식이 올바르지 않습니다.")
    private final String phone2nd;

    private final String job2nd;

    private final String name3rd;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "3차 담당자 휴대폰 형식이 올바르지 않습니다.")
    private final String phone3rd;

    private final String job3rd;

    private final String address;

    private final String zipCode;

    private final TradeType status;
}
