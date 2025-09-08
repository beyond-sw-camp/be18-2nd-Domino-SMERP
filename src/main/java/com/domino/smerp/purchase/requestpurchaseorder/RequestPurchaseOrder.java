package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.itemrpocrossedtable.ItemRpoCrossedTable;
import com.domino.smerp.purchase.requestpurchaseorder.constants.RequestPurchaseOrderStatus;
import com.domino.smerp.user;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    name = "request_purchase_order",
    indexes = {
        @Index(name = "idx_rpo_user_id", columnList = "user_id"),
        @Index(name = "idx_rpo_delivery_at", columnList = "delivery_at"),
        @Index(name = "idx_rpo_status", columnList = "status")
    }
)
public class RequestPurchaseOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rpo_id", nullable = false)
  private Long rpoId; // 구매요청 PK

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user; // 사용자 FK

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt; // 생성일시 (UTC)

  @Column(name = "updated_at")
  private Instant updatedAt; // 수정일시 (UTC)

  @Column(name = "delivery_at", nullable = false)
  private Instant deliveryAt; // 납기요청일시 (UTC)

  @Size(max = 100)
  @Column(name = "remark", length = 100)
  private String remark; // 비고

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private RequestPurchaseOrderStatus status; // 상태 (기본: PENDING)

  @OneToMany(mappedBy = "requestPurchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ItemRpoCrossedTable> items = new ArrayList<>();

  // ====== Lifecycle ======
  @PrePersist
  void onCreate() {
    Instant now = Instant.now();
    this.createdAt = now;
    if (this.deliveryAt == null) {
      this.deliveryAt = now;
    }
    if (this.status == null) {
      this.status = RequestPurchaseOrderStatus.PENDING;
    }
    if (this.updatedAt == null) {
      this.updatedAt = this.createdAt;
    }
  }

  // ====== 도메인 메서드 ======
  public void changeDeliveryAt(final Instant deliveryAt) {
    this.deliveryAt = deliveryAt;
  }

  public void changeRemark(final String remark) {
    this.remark = remark;
  }

  public void changeDocumentDate(final Instant docDate) {
    this.updatedAt = docDate;
  }

  public void markApproved() {
    this.status = RequestPurchaseOrderStatus.APPROVED;
  }

  public void markCompleted() {
    this.status = RequestPurchaseOrderStatus.COMPLETED;
  }

  public void markReturned(final String reason) {
    this.status = RequestPurchaseOrderStatus.RETURNED;
    appendRemark(reason);
  }

  public void revertToPending(final String reason) {
    this.status = RequestPurchaseOrderStatus.PENDING;
    appendRemark(reason);
  }

  private void appendRemark(final String msg) {
    if (msg == null || msg.isBlank()) {
      return;
    }
    if (this.remark == null || this.remark.isBlank()) {
      this.remark = msg;
    } else {
      String joined = this.remark + " | " + msg;
      this.remark = joined.length() <= 100 ? joined : joined.substring(0, 100);
    }
  }

  public void addItem(final ItemRpoCrossedTable item) {
    this.items.add(item);
  }
}
