package com.domino.smerp.user.dto.request;

import com.domino.smerp.user.constants.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UpdateUserRequest {
    @NotBlank(message = "주소는 필수 입력입니다.")
    private final String address;

    @NotBlank(message = "전화번호는 필수 입력입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private final String phone;

    @NotBlank(message = "부서명은 필수 입력입니다.")
    private final String deptTitle;

    @NotBlank(message = "역할(Role)은 필수 입력입니다.")
    private final UserRole role;

    private final LocalDate fireDate;
}
