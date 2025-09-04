package com.domino.smerp.order;

import com.domino.smerp.client.Client;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;   // ✅ constants 패키지로 이동한 Enum 사용

    @Column(nullable = false)
    private LocalDate createdDate;

    private LocalDate updatedDate;

    @Column(nullable = false)
    private LocalDate deliveryDate;

    private String remark;

    public void updateStatus(OrderStatus status) {
        this.status = status;
        this.updatedDate = LocalDate.now();
    }
}
