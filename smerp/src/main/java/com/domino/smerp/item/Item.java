package com.domino.smerp.item;

import com.domino.smerp.item.constants.ItemAct;
import com.domino.smerp.item.constants.SafetyStockAct;
import com.domino.smerp.item.dto.request.UpdateItemRequest;
import com.domino.smerp.item.dto.request.UpdateItemStatusRequest;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "item")
@SQLRestriction("is_deleted = false")
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Long itemId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_status_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private ItemStatus itemStatus;

  @Column(name = "name", nullable = false, length = 60)
  private String name;

  @Column(name = "specification", length = 100)
  private String specification;

  @Column(name = "unit", nullable = false, length = 10)
  private String unit;

  @Column(name = "inbound_unit_price", precision = 12, scale = 2)
  private BigDecimal inboundUnitPrice;

  @Column(name = "outbound_unit_price", precision = 12, scale = 2)
  private BigDecimal outboundUnitPrice;

  @Column(name = "created_date", nullable = false, updatable = false)
  private Instant createdDate;

  @Column(name = "updated_date")
  private Instant updatedDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "item_act", nullable = false)
  private ItemAct itemAct;

  @Column(name = "safety_stock", nullable = false)
  private Integer safetyStock;

  @Enumerated(EnumType.STRING)
  @Column(name = "safety_stock_act", nullable = false)
  private SafetyStockAct safetyStockAct;

  @Column(name = "rfid", nullable = false, unique = true,length = 30)
  private String rfid;

  @Column(name = "group_name1", length = 50)
  private String groupName1;

  @Column(name = "group_name2", length = 50)
  private String groupName2;

  @Column(name = "group_name3", length = 50)
  private String groupName3;

  @Builder.Default
  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = false;

  // 품목 수정
  public void updateItem(UpdateItemRequest request, ItemStatus itemStatus) {
    if (itemStatus != null) this.itemStatus = itemStatus;
    if (request.getName() != null) this.name = request.getName();
    if (request.getSpecification() != null) this.specification = request.getSpecification();
    if (request.getUnit() != null) this.unit = request.getUnit();
    if (request.getInboundUnitPrice() != null) this.inboundUnitPrice = request.getInboundUnitPrice();
    if (request.getOutboundUnitPrice() != null) this.outboundUnitPrice = request.getOutboundUnitPrice();
    if (request.getRfid() != null) this.rfid = request.getRfid();
    if (request.getGroupName1() != null) this.groupName1 = request.getGroupName1();
    if (request.getGroupName2() != null) this.groupName2 = request.getGroupName2();
    if (request.getGroupName3() != null) this.groupName3 = request.getGroupName3();
  }


  // 품목 사용/비사용, 안전 재고를 다룹니다.
  public void updateStatus(UpdateItemStatusRequest request) {
    if (request.getItemAct() != null) {this.itemAct = ItemAct.fromLabel(request.getItemAct());}
    if (request.getSafetyStock() != null) {this.safetyStock = request.getSafetyStock();}
    if (request.getSafetyStockAct() != null) {this.safetyStockAct = SafetyStockAct.fromLabel(request.getSafetyStockAct());}
  }

  // 품목 삭제 (소프트딜리트)
  public void delete() {
    this.isDeleted = true;
  }

  // TODO: BaseEntity 상속
  @PrePersist
  private void onPrePersist() {
    this.createdDate = Instant.now();
  }

  @PreUpdate
  private void onPreUpdate() {
    this.updatedDate = Instant.now();
  }
}
