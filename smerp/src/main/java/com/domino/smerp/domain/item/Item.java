package com.domino.smerp.domain.item;

import com.domino.smerp.domain.item.dto.ItemRequestDto;
import com.domino.smerp.domain.item.enums.ItemAct;
import com.domino.smerp.domain.item.enums.SafetyStockAct;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item")
@Getter
@NoArgsConstructor
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Integer itemId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_status_id", nullable = false)
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

  @Column(name = "created_date", nullable = false)
  private LocalDate createdDate;

  @Column(name = "updated_date")
  private LocalDate updatedDate;

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

  @Builder
  public Item(ItemStatus itemStatus, String name, String specification, String unit,
      BigDecimal inboundUnitPrice, BigDecimal outboundUnitPrice, ItemAct itemAct,
      Integer safetyStock, SafetyStockAct safetyStockAct, String rfid, String groupName1,
      String groupName2, String groupName3) {
    this.itemStatus = itemStatus;
    this.name = name;
    this.specification = specification;
    this.unit = unit;
    this.inboundUnitPrice = inboundUnitPrice;
    this.outboundUnitPrice = outboundUnitPrice;
    this.itemAct = itemAct;
    this.safetyStock = safetyStock;
    this.safetyStockAct = safetyStockAct;
    this.rfid = rfid;
    this.groupName1 = groupName1;
    this.groupName2 = groupName2;
    this.groupName3 = groupName3;
  }

  // Setter 금지 규칙에 따라, 데이터 수정을 위한 전용 메서드를 사용합니다.
  public void updateDetails(ItemRequestDto dto, ItemStatus itemStatus) {
    if (itemStatus != null) {
      this.itemStatus = itemStatus;
    }
    if (dto.getName() != null) {
      this.name = dto.getName();
    }
    if (dto.getSpecification() != null) {
      this.specification = dto.getSpecification();
    }
    if (dto.getUnit() != null) {
      this.unit = dto.getUnit();
    }
    if (dto.getInboundUnitPrice() != null) {
      this.inboundUnitPrice = dto.getInboundUnitPrice();
    }
    if (dto.getOutboundUnitPrice() != null) {
      this.outboundUnitPrice = dto.getOutboundUnitPrice();
    }
    if (dto.getSafetyStock() != null) {
      this.safetyStock = dto.getSafetyStock();
    }
    if (dto.getRfid() != null) {
      this.rfid = dto.getRfid();
    }
    if (dto.getGroupName1() != null) {
      this.groupName1 = dto.getGroupName1();
    }
    if (dto.getGroupName2() != null) {
      this.groupName2 = dto.getGroupName2();
    }
    if (dto.getGroupName3() != null) {
      this.groupName3 = dto.getGroupName3();
    }
  }

  public void updateStatus(ItemAct status) {
    this.itemAct = status;
  }

  // JPA 엔티티 라이프사이클을 활용하여 날짜를 자동으로 관리합니다.
  @PrePersist
  protected void onCreate() {
    this.createdDate = LocalDate.now();
    this.updatedDate = LocalDate.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedDate = LocalDate.now();
  }

}
