package com.domino.smerp.order;

import com.domino.smerp.client.Client;
import com.domino.smerp.itemorder.ItemOrderCrossedTable;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

;

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
    @JoinColumn(name = "client_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "delivery_date", nullable = false)
    private Instant deliveryDate;

    @Column(name = "remark")
    private String remark;

    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemOrderCrossedTable> orderItems = new ArrayList<>();

    // === 연관관계 편의 메서드 ===
    public void addOrderItem(ItemOrderCrossedTable orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }

    // === 전체 업데이트 메서드 ===
    public void updateAll(Instant orderDate,
                          Instant deliveryDate,
                          String remark,
                          OrderStatus status,
                          User newUser,
                          List<ItemOrderCrossedTable> newOrderItems) {
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.remark = remark;
        this.status = status;
        this.user = newUser;

        this.orderItems.clear();
        if (newOrderItems != null) {
            newOrderItems.forEach(this::addOrderItem);
        }

        this.updatedDate = Instant.now();
    }
}
