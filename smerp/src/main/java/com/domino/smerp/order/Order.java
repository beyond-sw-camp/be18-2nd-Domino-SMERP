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
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "remark")
    private String remark;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "updated_date")
    private LocalDate updatedDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemOrderCrossedTable> orderItems = new ArrayList<>();

    // === 연관관계 편의 메서드 ===
    public void addOrderItem(ItemOrderCrossedTable orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }

    // === 전체 업데이트 메서드 ===
    public void updateAll(LocalDate orderDate,
                          LocalDate deliveryDate,
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

        this.updatedDate = LocalDate.now();
    }
}
