package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.constants.RequestPurchaseOrderStatus;
import com.domino.smerp.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구매요청(RequestPurchaseOrder) 엔티티
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
    name = "request_purchase_order",
    indexes = {
        @Index(name = "idx_rpo_user_id", columnList = "user_id"),
        @Index(name = "idx_rpo_delivery_date", columnList = "delivery_date"),
        @Index(name = "idx_rpo_status", columnList = "status"),
        @Index(name = "idx_rpo_is_deleted", columnList = "is_deleted")
    }
)
public class RequestPurchaseOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rpo_id", nullable = false)
  private Long rpoId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private User user; // 사용자 FK

  @Column(name = "created_date", nullable = false, updatable = false)
  private Instant createdDate;

  @Column(name = "updated_date")
  private Instant updatedDate;

  @Column(name = "delivery_date", nullable = false)
  private Instant deliveryDate;

  @Size(max = 100)
  @Column(name = "remark", length = 100)
  private String remark;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private RequestPurchaseOrderStatus status;

  // ====== Soft Delete 관련 ======
  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted;

  @Column(name = "deleted_at")
  private Instant deletedAt; // 삭제 요청 시간

  @Column(name = "document_no", length = 30, nullable = false)
  private String documentNo; // 전표번호

  // ====== Lifecycle ======
  @PrePersist
  void onCreate() {
    Instant now = Instant.now();
    this.createdDate = now;
    if (this.deliveryDate == null) {
      this.deliveryDate = now;
    }
    if (this.status == null) {
      this.status = RequestPurchaseOrderStatus.PENDING;
    }
    if (this.updatedDate == null) {
      this.updatedDate = this.createdDate;
    }
    this.isDeleted = false;
  }

  // ====== 도메인 메서드 ======
  public void updateDeliveryDate(final Instant deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public void updateRemark(final String remark) {
    this.remark = remark;
  }

  public void updateDocumentDate(final Instant docDate) {
    this.updatedDate = docDate;
  }

  public void updateStatusToApproved() {
    this.status = RequestPurchaseOrderStatus.APPROVED;
  }

  public void updateStatusToCompleted() {
    this.status = RequestPurchaseOrderStatus.COMPLETED;
  }

  public void updateStatusToReturned(final String reason) {
    this.status = RequestPurchaseOrderStatus.RETURNED;
    appendRemark(reason);
  }

  public void updateStatusToPending(final String reason) {
    this.status = RequestPurchaseOrderStatus.PENDING;
    appendRemark(reason);
  }

  // ====== Soft Delete ======
  public void markAsDeleted() {
    this.isDeleted = true;
    this.deletedAt = Instant.now();
  }

  public boolean isEligibleForPermanentDeletion() {
    if (!this.isDeleted || this.deletedAt == null) {
      return false;
    }
    Instant now = Instant.now();
    return now.isAfter(this.deletedAt.plusSeconds(60L * 60 * 168)); // 7일(168시간)
  }

  // ====== private util ======
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
}
