package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.client.Client;
import com.domino.smerp.purchase.requestorder.constants.RequestOrderStatus;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
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
        @Index(name = "idx_ro_delivery_date", columnList = "delivery_date"),
        @Index(name = "idx_ro_status", columnList = "status")
    }
)
public class RequestOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ro_id", nullable = false)
  private Long roId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "rpo_id",
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private RequestPurchaseOrder requestPurchaseOrder;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "client_id",
      nullable = false,
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private Client client;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      nullable = false,
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private User user;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private RequestOrderStatus status;

  @Column(name = "created_date", nullable = false, updatable = false)
  private Instant createdDate;

  @Column(name = "updated_date")
  private Instant updatedDate;

  @Column(name = "delivery_date", nullable = false)
  private Instant deliveryDate;

  @Size(max = 100)
  @Column(name = "remark", length = 100)
  private String remark;

  @PrePersist
  void onCreate() {
    Instant now = Instant.now();
    this.createdDate = now;
    if (this.deliveryDate == null) {
      this.deliveryDate = now;
    }
    if (this.status == null) {
      this.status = RequestOrderStatus.PENDING;
    }
    if (this.updatedDate == null) {
      this.updatedDate = this.createdDate;
    }
  }

  // ====== 도메인 메서드 ======
  public void updateDeliveryDate(final Instant deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public void updateRemark(final String remark) {
    this.remark = remark;
  }

  public void updateStatus(final RequestOrderStatus status) {
    this.status = status;
  }

  public void updateDocumentDate(final Instant docDate) {
    this.updatedDate = docDate;
  }
}
