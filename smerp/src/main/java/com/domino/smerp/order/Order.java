package com.domino.smerp.order;

import com.domino.smerp.client.Client;
import com.domino.smerp.itemorder.ItemOrderCrossedTable;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;   // ✅ 기본값 PENDING


    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;   // ✅ 주문 일자 추가

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "remark")
    private String remark;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    // ✅ 교차 테이블 연관관계
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemOrderCrossedTable> orderItems = new ArrayList<>();

    // === 편의 메서드 ===
    public void addOrderItem(ItemOrderCrossedTable orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this); // 역방향 설정
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
        this.updatedDate = LocalDate.now();
    }
}
