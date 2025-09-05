package com.domino.smerp.purchase.itemrpocrossedtablerequest;

import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
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
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;

/**
 * 품목-구매요청 교차테이블 Entity 구매요청(RequestPurchaseOrder)과 품목(Item)을 연결
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "item_rpo_crossed_table")
public class ItemRpoCrossedTable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_rpo_id", nullable = false)
  private Long itemRpoId;  // 교차테이블 PK

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rpo_id", nullable = false)
  private RequestPurchaseOrder requestPurchaseOrder;  // 구매요청 FK

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
