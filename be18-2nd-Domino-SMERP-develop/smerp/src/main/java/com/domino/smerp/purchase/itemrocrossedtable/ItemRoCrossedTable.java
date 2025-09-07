package com.domino.smerp.purchase.itemrocrossedtable;

import com.domino.smerp.item.Item;
import com.domino.smerp.purchase.requestorder.RequestOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 발주-품목 교차테이블 Entity RequestOrder ↔ Item 연결
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "item_ro_crossed_table")
public class ItemRoCrossedTable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_ro_id", nullable = false)
  private Long itemRoId;  // 교차테이블 PK

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ro_id", nullable = false)
  private RequestOrder requestOrder;  // 발주 FK

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;  // 품목 FK

  @Column(name = "qty", nullable = false)
  private int qty;  // 요청 수량

  // ====== 도메인 메서드 ======
  public void updateQty(final int qty) {
    this.qty = qty;
  }
}
