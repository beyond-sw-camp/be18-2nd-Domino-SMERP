package com.domino.smerp.user;

import com.domino.smerp.client.Client;
import com.domino.smerp.log.audit.AuditLogEntityListener;
import com.domino.smerp.user.constants.UserRole;
import com.domino.smerp.user.dto.request.UpdateUserRequest;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;

@Entity
@Getter
@Builder
@ToString
@Audited
@EntityListeners(AuditLogEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, unique = true, length = 60)
    private String email;

    @Column(nullable = false, unique = true, length = 15)
    private String phone;

    @Column(nullable = false, length = 120)
    private String address;

    @Column(nullable = false, unique = true)
    private String ssn;

    @Column(nullable = false)
    private LocalDate hireDate;

    private LocalDate fireDate;

    @Column(nullable = false, unique = true, length = 20)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String deptTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(unique = true)
    private String empNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Client client;

    public void updateUser(UpdateUserRequest request) {

        if (request.getAddress() != null) {
            this.address = request.getAddress();
        }
        if (request.getPhone() != null) {
            this.phone = request.getPhone();
        }
        if (request.getDeptTitle() != null) {
            this.deptTitle = request.getDeptTitle();
        }
        if (request.getRole() != null) {
            this.role = request.getRole();
        }
        if (request.getFireDate() != null) {
            this.fireDate = request.getFireDate();
        }
    }
    public void updateClient(Client client) {
        this.client = client;
    }
}
