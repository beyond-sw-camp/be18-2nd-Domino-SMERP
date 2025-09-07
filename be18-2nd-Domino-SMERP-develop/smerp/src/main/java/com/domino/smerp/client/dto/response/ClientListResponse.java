package com.domino.smerp.client.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ClientListResponse {
    private final String businessNumber;
    private final String companyName;
    private final String phone;
    private final String ceoName;
    private final String address;
    private final String zipCode;
}
