package com.domino.smerp.item;

import com.domino.smerp.item.constants.ItemAct;
import com.domino.smerp.item.constants.SafetyStockAct;
import com.domino.smerp.item.dto.request.ItemRequest;
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


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "item")
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

  @Column(name = "rfid", nullable = false, length = 30)
  private String rfid;

  @Column(name = "group_name1", length = 50)
  private String groupName1;

  @Column(name = "group_name2", length = 50)
  private String groupName2;

  @Column(name = "group_name3", length = 50)
  private String groupName3;

  // 품목 수정
  public void updateItem(ItemRequest dto, ItemStatus itemStatus) {
    if (itemStatus != null) this.itemStatus = itemStatus;
    if (dto.getName() != null) this.name = dto.getName();
    if (dto.getSpecification() != null) this.specification = dto.getSpecification();
    if (dto.getUnit() != null) this.unit = dto.getUnit();
    if (dto.getInboundUnitPrice() != null) this.inboundUnitPrice = dto.getInboundUnitPrice();
    if (dto.getOutboundUnitPrice() != null) this.outboundUnitPrice = dto.getOutboundUnitPrice();
    if (dto.getRfid() != null) this.rfid = dto.getRfid();
    if (dto.getGroupName1() != null) this.groupName1 = dto.getGroupName1();
    if (dto.getGroupName2() != null) this.groupName2 = dto.getGroupName2();
    if (dto.getGroupName3() != null) this.groupName3 = dto.getGroupName3();
  }

  // 품목 사용/비사용, 안전 재고를 다룹니다.
  public void updateStatus(UpdateItemStatusRequest request) {
    if (request.getItemAct() != null) {this.itemAct = ItemAct.fromLabel(request.getItemAct());}
    if (request.getSafetyStock() != null) {this.safetyStock = request.getSafetyStock();}
    if (request.getSafetyStockAct() != null) {this.safetyStockAct = SafetyStockAct.fromLabel(request.getSafetyStockAct());}
  }

  @PrePersist
  private void onPrePersist() {
    this.createdDate = Instant.now();
  }

  @PreUpdate
  private void onPreUpdate() {
    this.updatedDate = Instant.now();
  }
}
