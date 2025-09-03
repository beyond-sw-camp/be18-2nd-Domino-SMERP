package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_rpo_crossed_table")
@Getter
@Setter
@NoArgsConstructor
public class ItemRpoCrossedTable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "itemrpo_id")
  private Long itemrpoId;

  // 구매요청 FK
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rpo_id", nullable = false)
  private RequestPurchaseOrder rpo;

  // 품목 FK (Item 엔티티는 나중에 merge 예정)
  @Column(name = "item_id", nullable = false)
  private Long itemId;

  // 수량
  @Column(name = "qty", nullable = false)
  private Integer qty;
}
