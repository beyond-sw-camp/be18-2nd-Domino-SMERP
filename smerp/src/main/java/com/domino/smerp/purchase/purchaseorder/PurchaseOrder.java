package com.domino.smerp.purchase.purchaseorder;

import com.domino.smerp.purchase.requestorder.RequestOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구매(PurchaseOrder) 엔티티 - 발주(RequestOrder)와 1:1 매핑 - 발주 없는 구매는 존재할 수 없음
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    name = "purchase_order",
    indexes = {
        @Index(name = "idx_po_ro_id", columnList = "ro_id")
    }
)
public class PurchaseOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "po_id", nullable = false)
  private Long poId; // 구매 PK

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ro_id", nullable = false, unique = true)
  private RequestOrder requestOrder; // 발주 전표 (1:1)

  @Column(name = "created_date", nullable = false, updatable = false)
  private Instant createdDate; // 생성일자 (UTC)

  @Column(name = "updated_date")
  private Instant updatedDate; // 수정일자 (UTC)

  @Column(name = "qty", nullable = false)
  private int qty; // 수량 (발주와 다를 수 있음)

  @Column(name = "surtax", nullable = false, precision = 12, scale = 2)
  private BigDecimal surtax; // 부가세

  @Column(name = "price", nullable = false, precision = 12, scale = 2)
  private BigDecimal price; // 금액 (qty * 단가)

  @Size(max = 100)
  @Column(name = "remark", length = 100)
  private String remark; // 비고

  // ====== Lifecycle ======
  @PrePersist
  void onCreate() {
    Instant now = Instant.now();
    this.createdDate = now;
    if (this.updatedDate == null) {
      this.updatedDate = this.createdDate;
    }
  }

  // ====== 도메인 메서드 ======
  public void updateQty(final int qty) {
    this.qty = qty;
  }

  public void updateSurtax(final BigDecimal surtax) {
    this.surtax = surtax;
  }

  public void updatePrice(final BigDecimal price) {
    this.price = price;
  }

  public void updateRemark(final String remark) {
    this.remark = remark;
  }

  public void updateDocumentDate(final Instant docDate) {
    this.updatedDate = docDate;
  }
}
