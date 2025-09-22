package com.domino.smerp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private final String loginId;
    private final String userName;
    private final String role;
}
