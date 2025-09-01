package com.domino.smerp.user;

import com.domino.smerp.client.Client;
import com.domino.smerp.user.constants.UserRole;
import com.domino.smerp.user.dto.request.UpdateUserRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String ssn;

    @Column(nullable = false)
    private LocalDate hireDate;

    @Column(nullable = true)
    private LocalDate fireDate;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String deptTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = true)
    private Client client;

    public void updateUser(UpdateUserRequest request){
        if (request.getAddress() != null) this.address = request.getAddress();
        if (request.getPhone() != null) this.phone = request.getPhone();
        if (request.getDeptTitle() != null) this.deptTitle = request.getDeptTitle();
        if (request.getRole() != null) this.role = request.getRole();
        if (request.getFireDate() != null) this.fireDate = request.getFireDate();
    }
}
