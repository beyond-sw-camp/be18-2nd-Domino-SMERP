package com.domino.smerp.salesorder;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.order.Order;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@ToString
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
    private Long soId;

    // 주문 참조 (1:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;

    @Column(name = "document_no", nullable = false, length = 30, unique = true)
    private String documentNo;

    @Column(name = "remark", length = 100)
    private String remark;

    @Column(name = "warehouse_name", nullable = false, length = 50)
    private String warehouseName;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public void updateAll(String remark, String warehouseName) {
        if (remark != null) this.remark = remark;
        if (warehouseName != null) this.warehouseName = warehouseName;
    }

    public void updateDocumentInfo(String newDocumentNo) {
        this.documentNo = newDocumentNo;
    }
}
