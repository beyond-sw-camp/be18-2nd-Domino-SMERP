package com.domino.smerp.logging.snapshot;

import com.domino.smerp.user.User;
import com.domino.smerp.user.constants.UserRole;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSnapshot {

    private String empNo;

    private String name;

    private String deptTitle;

    private UserRole role;

    private String phone;

    private String address;

    private LocalDate fireDate;

    private String companyName;

    public static UserSnapshot from(User user) {
        return UserSnapshot.builder()
            .empNo(user.getEmpNo())
            .name(user.getName())
            .deptTitle(user.getDeptTitle())
            .role(user.getRole())
            .phone(user.getPhone())
            .address(user.getAddress())
            .fireDate(user.getFireDate())
            .companyName(
                user.getClient() != null
                    ? user.getClient().getCompanyName()
                    : null
            )
            .build();
    }
}