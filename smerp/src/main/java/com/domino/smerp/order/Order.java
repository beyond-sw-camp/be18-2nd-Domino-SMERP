package com.domino.smerp.order;

import com.domino.smerp.client.Client;
import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.itemorder.ItemOrder;
import com.domino.smerp.log.audit.AuditLogEntityListener;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.salesorder.SalesOrder;
import com.domino.smerp.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
//@ToString
//@Audited
//@EntityListeners(AuditLogEntityListener.class)
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

    @Column(name = "document_no", nullable = false, length = 30, unique = true)
    private String documentNo;

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

    @Column(name = "delivery_at", nullable = false)
    private Instant deliveryDate;

    @Column(name = "remark", length = 100)
    private String remark;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "order") // Cascade 제거
    @Builder.Default
    private List<ItemOrder> orderItems = new ArrayList<>();

    // 일대일 매핑
    @Builder.Default
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<SalesOrder> salesOrders = new ArrayList<>();

    // 양방향 연관관계 세팅 메서드
    public void addOrderItem(ItemOrder orderItem) {
        this.orderItems.add(orderItem);
        orderItem.assignOrder(this);
    }

    //  도메인 계산 메소드
    public BigDecimal getTotalAmount() {
        return orderItems.stream()
                .map(ItemOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public ItemOrder getFirstItem() {
        return orderItems.stream()
                .findFirst()
                .orElse(null);
    }

    // 첫번째 품목명 가져오기
    public String getFirstItemName() {
        ItemOrder firstItem = this.getFirstItem();
        return (firstItem != null) ? firstItem.getItem().getName() : null;
    }

    // 품목 갯수 카운트
    public int getOtherItemCount() {
        return (this.getOrderItems().size() > 1)
                ? this.getOrderItems().size() - 1
                : 0;
    }

    // 전체 업데이트 메서드 null 확인으로 수정
    // PATCH 방식: null이 들어오면 기존 값 유지
    public void updateDocumentInfo(String newDocumentNo) {
        this.documentNo = newDocumentNo;
    }

    public void updateAll(Instant deliveryDate,
                          String remark,
                          OrderStatus status,
                          User newUser,
                          List<ItemOrder> newOrderItems) {

        if (deliveryDate != null) {
            this.deliveryDate = deliveryDate;
        }
        if (remark != null) {
            this.remark = remark;
        }
        if (status != null) {
            this.status = status;
        }
        if (newUser != null) {
            this.user = newUser;
        }
        if (newOrderItems != null && !newOrderItems.isEmpty()) {
            this.orderItems.clear();
            newOrderItems.forEach(this::addOrderItem);
        }
    }
}
