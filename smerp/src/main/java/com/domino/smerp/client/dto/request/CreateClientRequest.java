package com.domino.smerp.client.dto.request;

import com.domino.smerp.client.constants.TradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateClientRequest {
    private final String businessNumber;
    private final String companyName;
    private final String phone;
    private final String ceoName;
    private final String ceoPhone;
    private final String name1st;
    private final String phone1st;
    private final String job1st;
    private final String name2nd;
    private final String phone2nd;
    private final String job2nd;
    private final String name3rd;
    private final String phone3rd;
    private final String job3rd;
    private final String address;
    private final String zipCode;
    private final TradeType status;
}
