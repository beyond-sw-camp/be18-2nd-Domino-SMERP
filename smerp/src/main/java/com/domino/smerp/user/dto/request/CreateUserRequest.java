package com.domino.smerp.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "이름은 필수 입력입니다.")
    private final String name;

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private final String email;

    @NotBlank(message = "전화번호는 필수 입력입니다.")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    private final String phone;

    @NotBlank(message = "주소는 필수 입력입니다.")
    private final String address;

    @NotBlank(message = "주민번호는 필수 입력입니다.")
    @Pattern(regexp = "^\\d{6}-\\d{7}$", message = "주민번호 형식이 올바르지 않습니다.")
    private final String ssn;

    @NotBlank(message = "로그인 아이디는 필수 입력입니다.")
    private final String loginId;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자여야 합니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$",
        message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private final String password;

    @NotNull(message = "입사일은 필수 입력입니다.")
    private final LocalDate hireDate;

    private final LocalDate fireDate;

    @NotBlank(message = "부서명은 필수 입력입니다.")
    private final String deptTitle;

    @NotBlank(message = "역할(Role)은 필수 입력입니다.")
    private final String role;

    private final Long clientId;
}
