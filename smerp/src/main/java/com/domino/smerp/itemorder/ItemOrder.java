package com.domino.smerp.itemorder;

import com.domino.smerp.item.Item;
import com.domino.smerp.order.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "item_order_crossed_table")
public class ItemOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Item item;

    @Column(name = "qty", nullable = false, precision = 12, scale = 3)
    private BigDecimal qty;

    @Column(name = "special_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal specialPrice;

    // == 연관관계 메서드 ==
    public void assignOrder(Order order) {
        this.order = order;
    }

    // == 도메인 수정 메서드 (Setter 금지) ==
    public void updateQty(BigDecimal qty) {
        if (qty != null) this.qty = qty;
    }

    public void updateSpecialPrice(BigDecimal specialPrice) {
        if (specialPrice != null) {
            this.specialPrice = specialPrice;
        }
    }

    // == 계산 메서드 ==
    public BigDecimal getSupplyAmount() {
        return qty.multiply(specialPrice);
    }

    public BigDecimal getTax() {
        return getSupplyAmount().multiply(BigDecimal.valueOf(0.1));
    }

    public BigDecimal getTotalAmount() {
        return getSupplyAmount().add(getTax());
    }
}
