package com.domino.smerp.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserListResponse {
    private final String name;
    private final String email;
    private final String phone;
    private final String address;
    private final String deptTitle;
    private final String role;
}
