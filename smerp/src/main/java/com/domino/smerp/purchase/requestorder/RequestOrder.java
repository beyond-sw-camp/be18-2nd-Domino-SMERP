package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.client.Client;
import com.domino.smerp.user.User;
import com.domino.smerp.purchase.requestorder.constants.RequestOrderStatus;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 발주(RequestOrder) 엔티티
 * - 매니저 권한으로 구매요청(RequestPurchaseOrder)을 불러와 확정하는 전표
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    name = "request_order",
    indexes = {
        @Index(name = "idx_ro_user_id", columnList = "user_id"),
        @Index(name = "idx_ro_client_id", columnList = "client_id"),
        @Index(name = "idx_ro_rpo_id", columnList = "rpo_id"),
        @Index(name = "idx_ro_delivery_at", columnList = "delivery_at"),
        @Index(name = "idx_ro_status", columnList = "status")
    }
)
public class RequestOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ro_id", nullable = false)
  private Long roId; // 발주 PK

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rpo_id", nullable = false)
  private RequestPurchaseOrder requestPurchaseOrder; // 구매요청 FK

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_id", nullable = false)
  private Client client; // 거래처 FK

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user; // 작성자(매니저) FK

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
  private RequestOrderStatus status; // 상태 (PENDING, APPROVED, COMPLETED, RETURNED)

  @OneToMany(mappedBy = "requestOrder", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ItemRoCrossedTable> items = new ArrayList<>();

  // ====== Lifecycle ======
  @PrePersist
  void onCreate() {
    Instant now = Instant.now();
    this.createdAt = now;
    if (this.deliveryAt == null) {
      this.deliveryAt = now;
    }
    if (this.status == null) {
      this.status = RequestOrderStatus.PENDING;
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
    this.status = RequestOrderStatus.APPROVED;
  }

  public void markCompleted() {
    this.status = RequestOrderStatus.COMPLETED;
  }

  public void markReturned(final String reason) {
    this.status = RequestOrderStatus.RETURNED;
    appendRemark(reason);
  }

  public void revertToPending(final String reason) {
    this.status = RequestOrderStatus.PENDING;
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

  public void addItem(final ItemRoCrossedTable item) {
    this.items.add(item);
  }
}
