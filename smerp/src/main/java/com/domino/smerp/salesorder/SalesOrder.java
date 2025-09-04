package com.domino.smerp.salesorder;

import com.domino.smerp.order.Order;
import com.domino.smerp.salesorder.constants.SalesOrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "sales_order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "so_id", nullable = false)
    private Long soId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false)
    private Double surtax;

    @Column(nullable = false)
    private Double price;

    private String remark;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalesOrderStatus status = SalesOrderStatus.APPROVED;  // ✅ 기본값 설정

    @Column(nullable = false)
    private LocalDate createdDate;

    private LocalDate updatedDate;

    public void updateStatus(SalesOrderStatus status) {
        this.status = status;
        this.updatedDate = LocalDate.now();
    }
}
