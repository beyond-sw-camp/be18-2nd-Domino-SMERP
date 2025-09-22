package com.domino.smerp.purchase.itemrequestpurchaseorder;

import com.domino.smerp.item.Item;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "item_rpo_crossed_table")
public class ItemRequestPurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_rpo_id", nullable = false)
    private Long itemRpoId; // 품목구매요청PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rpo_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private RequestPurchaseOrder requestPurchaseOrder; // 구매요청 FK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Item item; // 품목 FK

    @Column(name = "inbound_unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal inboundUnitPrice; // 입고 단가

    @Column(name = "special_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal specialPrice; // 특별단가

    @Column(name = "qty", nullable = false, precision = 12, scale = 3)
    private BigDecimal qty; // 수량

    // ===== 도메인 메서드 =====
    public void updateQty(final BigDecimal qty) {
        this.qty = qty;
    }
}
