package com.domino.smerp.item.entity;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.item.constants.ItemAct;
import com.domino.smerp.item.constants.SafetyStockAct;
import com.domino.smerp.item.dto.request.CreateItemRequest;
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
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
public class Item extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Long itemId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_status_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private ItemStatus itemStatus;

  @Column(name = "name", nullable = false, unique = true, length = 60)
  private String name;

  // TODO: 품목 코드 추가(PK 대신 품목 코드로 조회 예정)
//  @Column(name = "item_code", nullable = false, unique = true, length = 20)
//  private String itemCode;

  @Column(name = "specification", length = 100)
  private String specification;

  @Column(name = "unit", nullable = false, length = 10)
  private String unit;

  @Column(name = "inbound_unit_price", precision = 12, scale = 2)
  private BigDecimal inboundUnitPrice;

  @Column(name = "outbound_unit_price", precision = 12, scale = 2)
  private BigDecimal outboundUnitPrice;

  @Enumerated(EnumType.STRING)
  @Column(name = "item_act", nullable = false)
  private ItemAct itemAct;

  @Column(name = "safety_stock", nullable = false, precision = 12, scale = 3)
  private BigDecimal safetyStock;

  @Enumerated(EnumType.STRING)
  @Column(name = "safety_stock_act", nullable = false)
  private SafetyStockAct safetyStockAct;

  @Column(name = "rfid", nullable = false, unique = true, length = 30)
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


  // 품목 생성
  public static Item create(CreateItemRequest request, ItemStatus itemStatus) {
    return Item.builder()
        .itemStatus(itemStatus)
        .name(request.getName())
        // .itemCode(request.getItemCode())
        .specification(request.getSpecification())
        .unit(request.getUnit())
        .inboundUnitPrice(request.getInboundUnitPrice())
        .outboundUnitPrice(request.getOutboundUnitPrice())
        .itemAct(ItemAct.fromLabel(request.getItemAct()))
        .safetyStock(request.getSafetyStock())
        .safetyStockAct(SafetyStockAct.fromLabel(request.getSafetyStockAct()))
        .rfid(request.getRfid())
        .groupName1(request.getGroupName1())
        .groupName2(request.getGroupName2())
        .groupName3(request.getGroupName3())
        .build();
  }


  // 품목 수정
  public void updateItem(UpdateItemRequest request, ItemStatus itemStatus) {
    if (itemStatus != null) {
      this.itemStatus = itemStatus;
    }
    if (request.getName() != null) {
      this.name = request.getName();
    }
//    if (request.getItemCode() != null) {
//      this.itemCode = request.getItemCode();
//    }
    if (request.getSpecification() != null) {
      this.specification = request.getSpecification();
    }
    if (request.getUnit() != null) {
      this.unit = request.getUnit();
    }
    if (request.getInboundUnitPrice() != null) {
      this.inboundUnitPrice = request.getInboundUnitPrice();
    }
    if (request.getOutboundUnitPrice() != null) {
      this.outboundUnitPrice = request.getOutboundUnitPrice();
    }
    if (request.getRfid() != null) {
      this.rfid = request.getRfid();
    }
    if (request.getGroupName1() != null) {
      this.groupName1 = request.getGroupName1();
    }
    if (request.getGroupName2() != null) {
      this.groupName2 = request.getGroupName2();
    }
    if (request.getGroupName3() != null) {
      this.groupName3 = request.getGroupName3();
    }
  }


  // 품목 사용/비사용, 안전 재고를 다룹니다.
  public void updateStatus(final UpdateItemStatusRequest request) {
    if (request.getItemAct() != null) {
      this.itemAct = ItemAct.fromLabel(request.getItemAct());
    }
    if (request.getSafetyStock() != null) {
      this.safetyStock = request.getSafetyStock();
    }
    if (request.getSafetyStockAct() != null) {
      this.safetyStockAct = SafetyStockAct.fromLabel(request.getSafetyStockAct());
    }
  }

  // 품목 삭제 (소프트딜리트)
  public void delete() {
    this.isDeleted = true;
  }

}