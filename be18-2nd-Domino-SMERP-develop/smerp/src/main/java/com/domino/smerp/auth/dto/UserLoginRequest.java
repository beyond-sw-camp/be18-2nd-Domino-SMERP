package com.domino.smerp.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserLoginRequest {
    private final String loginId;
    private final String password;
}
