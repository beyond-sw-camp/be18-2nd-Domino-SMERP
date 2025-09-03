package com.domino.smerp.purchase.requestpurchaseorder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "request_purchase_order",
    indexes = {
        @Index(name = "idx_rpo_user_id", columnList = "user_id"),
        @Index(name = "idx_rpo_delivery_date", columnList = "delivery_date"),
        @Index(name = "idx_rpo_status", columnList = "status")
    }
)
public class RequestPurchaseOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rpo_id")
  private Long id;

  /**
   * 사용자 FK
   */
  @NotNull
  @Column(name = "user_id", nullable = false)
  private Long userId;

  /**
   * 로그용: 항상 오늘(today)로 세팅
   */
  @NotNull
  @Column(name = "created_date", nullable = false)
  private LocalDate createdDate;

  /**
   * 전표일자(사용자 입력). 정책: 생성 시 비어 있으면 created_date로 기본 세팅
   */
  @Column(name = "updated_date")
  private LocalDate updatedDate;

  /**
   * 납기요청일자: 기본값 today
   */
  @NotNull
  @Column(name = "delivery_date", nullable = false)
  private LocalDate deliveryDate;

  /**
   * 비고(최대 100자)
   */
  @Size(max = 100)
  @Column(name = "remark", length = 100)
  private String remark;

  /**
   * 상태(기본값: PENDING)
   */
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private RequestPurchaseOrderStatus status;

  @Builder
  public RequestPurchaseOrder(Long userId,
      LocalDate deliveryDate,
      String remark,
      RequestPurchaseOrderStatus status,
      LocalDate updatedDate) {
    this.userId = userId;
    this.deliveryDate = deliveryDate;
    this.remark = remark;
    this.status = status;
    this.updatedDate = updatedDate; // null 허용 → @PrePersist에서 created_date로 기본 세팅
  }

  /* ====== Lifecycle ====== */
  @PrePersist
  void onCreate() {
    LocalDate today = LocalDate.now();
      if (this.createdDate == null) {
          this.createdDate = today;                   // 항상 today
      }
      if (this.deliveryDate == null) {
          this.deliveryDate = today;                  // 없으면 today
      }
      if (this.status == null) {
          this.status = RequestPurchaseOrderStatus.PENDING;
      }
      if (this.updatedDate == null) {
          this.updatedDate = this.createdDate;        // ✅ 정책 #3
      }
  }

  /* ====== 도메인 메서드 ====== */
  public void changeDeliveryDate(@NotNull LocalDate deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public void changeRemark(String remark) {
    this.remark = remark;
  }

  /**
   * 전표일자 명시 변경(PATCH로 노출)
   */
  public void changeDocumentDate(LocalDate docDate) {
    this.updatedDate = docDate;
  }

  public void markApproved() {
    this.status = RequestPurchaseOrderStatus.APPROVED;
  }

  public void markCompleted() {
    this.status = RequestPurchaseOrderStatus.COMPLETED;
  }

  public void markReturned(String reason) {
    this.status = RequestPurchaseOrderStatus.RETURNED;
    appendRemark(reason);
  }

  public void revertToPending(String reason) {
    this.status = RequestPurchaseOrderStatus.PENDING;
    appendRemark(reason);
  }

  private void appendRemark(String msg) {
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
