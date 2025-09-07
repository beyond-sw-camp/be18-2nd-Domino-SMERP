package com.domino.smerp.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateUserRequest {
    private final String name;
    private final String email;
    private final String phone;
    private final String address;
    private final String ssn;
    private final String loginId;
    private final String password;
    private final String hireDate;
    private final String fireDate;
    private final String deptTitle;
    private final String role;
    private final Long clientId;
}
