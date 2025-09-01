package com.domino.smerp.user.dto.request;

import com.domino.smerp.user.constants.UserRole;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdateUserRequest {
    private final String address;
    private final String phone;
    private final String deptTitle;
    private final UserRole role;
    private final LocalDate fireDate;
}
