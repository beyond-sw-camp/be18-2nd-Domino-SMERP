package com.domino.smerp.user.dto.response;

import com.domino.smerp.user.constants.UserRole;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserResponse {
    private final Long userId;
    private final String name;
    private final String email;
    private final String phone;
    private final String address;
    private final String ssn;
    private final LocalDate hireDate;
    private final LocalDate fireDate;
    private final String loginId;
    private final String deptTitle;
    private final UserRole role;
    private final String clientName;

}
