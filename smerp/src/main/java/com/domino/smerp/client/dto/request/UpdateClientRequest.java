package com.domino.smerp.client.dto.request;

import com.domino.smerp.client.constants.TradeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdateClientRequest {

    @NotBlank(message = "회사명은 필수 입력입니다.")
    private final String companyName;

    @NotBlank(message = "대표 전화번호는 필수 입력입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "대표 전화번호 형식이 올바르지 않습니다.")
    private final String phone;

    @NotBlank(message = "대표자 이름은 필수 입력입니다.")
    private final String ceoName;

    @NotBlank(message = "대표자 휴대폰은 필수 입력입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "대표자 휴대폰 형식이 올바르지 않습니다.")
    private final String ceoPhone;

    @NotBlank(message = "1차 담당자 이름은 필수 입력입니다.")
    private final String name1st;

    @NotBlank(message = "1차 담당자 휴대폰은 필수 입력입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "1차 담당자 휴대폰 형식이 올바르지 않습니다.")
    private final String phone1st;

    @NotBlank(message = "1차 담당자 직급은 필수 입력입니다.")
    private final String job1st;

    private final String name2nd;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "2차 담당자 휴대폰 형식이 올바르지 않습니다.")
    private final String phone2nd;

    private final String job2nd;

    private final String name3rd;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "3차 담당자 휴대폰 형식이 올바르지 않습니다.")
    private final String phone3rd;

    private final String job3rd;

    @NotBlank(message = "주소는 필수 입력입니다.")
    private final String address;

    @NotBlank(message = "우편번호는 필수 입력입니다.")
    private final String zipCode;

    @NotNull(message = "상태는 필수 입력입니다.")
    private final TradeType status;
}
