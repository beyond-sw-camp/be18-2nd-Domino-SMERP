package com.domino.smerp.user.dto.response;

import com.domino.smerp.user.constants.UserRole;
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
    private final UserRole role;
    private final String empNo;
}
