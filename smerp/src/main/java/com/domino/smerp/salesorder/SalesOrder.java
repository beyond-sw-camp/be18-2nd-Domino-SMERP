package com.domino.smerp.salesorder;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "sales_order")
@Getter
@SQLDelete(sql = "UPDATE sales_order SET is_deleted = true WHERE sales_order_id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@SQLRestriction("is_deleted = false")
public class SalesOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_order_id")
    private Long salesOrderId;

    // 주문 참조 (항상 동기화)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order orderId;

    // 판매일자 (주문일자와 같을 수도 있고, 실제 매출일을 따로 관리할 수도 있음)
    @Column(name = "sales_date", nullable = false)
    private java.time.Instant salesDate;

    @Column(name = "remark", length = 100)
    private String remark;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    // 편의 메서드: 주문에서 기본값 세팅
    public static SalesOrder fromOrder(Order order) {
        return SalesOrder.builder()
                .orderId(order)
                .salesDate(order.getCreatedAt())   // 주문 생성일자 그대로
                .remark(order.getRemark())
                .build();
    }
}
