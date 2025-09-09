package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.purchase.purchaseorder.PurchaseOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "request_order")
public class RequestOrder extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ro_id", nullable = false)
  private Long roId; // 발주 PK

  // PurchaseOrder 엔티티와 OneToOne 매핑
  // mappedBy를 사용하여 PurchaseOrder 엔티티의 'requestOrder' 필드에 의해 매핑됨을 명시합니다.
  @OneToOne(mappedBy = "requestOrder", fetch = FetchType.LAZY)
  private PurchaseOrder purchaseOrder;

}
