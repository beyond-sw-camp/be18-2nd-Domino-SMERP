package com.domino.smerp.purchase.itemrequestorder;

import com.domino.smerp.item.Item;
import com.domino.smerp.purchase.requestorder.RequestOrder;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "item_ro_crossed_table")
public class ItemRequestOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_ro_id", nullable = false)
    private Long itemRoId; // 품목발주 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ro_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private RequestOrder requestOrder; // 발주 참조

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Item item; // 품목 참조

    @Column(name = "qty", nullable = false, precision = 12, scale = 3)
    private BigDecimal qty; // 수량

    @Column(name = "inbound_unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal inboundUnitPrice; // 입고 단가

    // ====== 도메인 메서드 ======
    public void updateQty(BigDecimal qty) {
        this.qty = qty;
    }

    public void updateInboundUnitPrice(BigDecimal inboundUnitPrice) {
        this.inboundUnitPrice = inboundUnitPrice;
    }
}
