package com.domino.smerp.order;

import com.domino.smerp.client.Client;
import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.itemorder.ItemOrderCrossedTable;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`order`")
@Getter
@SQLDelete(sql = "UPDATE `order` SET is_deleted = true WHERE order_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@SQLRestriction("is_deleted = false")
public class Order extends BaseEntity {

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

    @Column(name = "delivery_date", nullable = false)
    private Instant deliveryDate;

    @Column(name = "remark", length = 100)
    private String remark;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "document_no", nullable = false, length = 30)
    private String documentNo;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemOrderCrossedTable> orderItems = new ArrayList<>();

    // === 연관관계 편의 메서드 ===
    public void addOrderItem(ItemOrderCrossedTable orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }

    // === 전표번호와 updatedAt 갱신 ===
    public void updateDocumentInfo(Instant baseDate, String newDocumentNo) {
        if (baseDate != null) {
            try {
                var field = BaseEntity.class.getDeclaredField("updatedAt");
                field.setAccessible(true);
                field.set(this, baseDate);
            } catch (Exception e) {
                throw new RuntimeException("updatedAt 갱신 실패", e);
            }
        }
        this.documentNo = newDocumentNo;
    }

    // === 전체 업데이트 메서드 === null 확인으로 수정
    public void updateAll(Instant orderDate,
                          Instant deliveryDate,
                          String remark,
                          OrderStatus status,
                          User newUser,
                          List<ItemOrderCrossedTable> newOrderItems) {
        this.deliveryDate = deliveryDate;
        this.remark = remark;
        this.status = status;
        this.user = newUser;

        this.orderItems.clear();
        if (newOrderItems != null) {
            newOrderItems.forEach(this::addOrderItem);
        }
    }
}
