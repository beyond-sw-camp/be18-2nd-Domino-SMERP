package com.domino.smerp.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(nullable = false)
    private String actor;                 // 로그인 사용자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ActionType action;            // CREATE/UPDATE/DELETE

    @Column(nullable = false)
    private String entity;                // 엔티티명 (ex. User, Order)

    @Column(nullable = false)
    private LocalDateTime doAt;           // 행위 시각

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String beforeData;            // UPDATE/DELETE의 변경 전 스냅샷

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String afterData;             // CREATE/UPDATE의 변경 후 스냅샷
}