package com.domino.smerp.itemorder;

import com.domino.smerp.item.Item;
import com.domino.smerp.order.Order;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_order_crossed_table")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ItemOrderCrossedTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private Integer qty;
}
