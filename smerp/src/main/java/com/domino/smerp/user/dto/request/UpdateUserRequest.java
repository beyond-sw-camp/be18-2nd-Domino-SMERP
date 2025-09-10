package com.domino.smerp.user.dto.request;

import com.domino.smerp.user.constants.UserRole;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdateUserRequest {
    private final String address;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private final String phone;

    private final String deptTitle;

    private final UserRole role;

    private final LocalDate fireDate;

    private final String companyName;
}
