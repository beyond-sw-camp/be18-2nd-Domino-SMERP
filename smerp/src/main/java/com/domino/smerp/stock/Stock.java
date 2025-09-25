package com.domino.smerp.stock;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.item.Item;
import com.domino.smerp.location.Location;
import com.domino.smerp.lotno.LotNumber;
import com.domino.smerp.lotno.repository.LotNumberRepository;
import com.domino.smerp.warehouse.Warehouse;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "stock")
@ToString(onlyExplicitlyIncluded = true)
public class Stock extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "stock_id")
  @ToString.Include
  private Long id;

  //lot no
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id",
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private LotNumber lotNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "location_id",
    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private Location location;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id",
    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private Item item;

  //수량
  @Builder.Default
  @Column(precision = 12, scale = 2, nullable = false)
  @ToString.Include
  private BigDecimal qty = BigDecimal.ZERO;

  //rfid
  //@Column(nullable = false)
  @ToString.Include
  private String rfid;

  //재고수불에서 합계용
  @Builder.Default
  private BigDecimal currentQty = BigDecimal.ZERO;

  //item 매핑 필요한가
  //item 생성하는 로직 시 stock까지 create 하도록 확인
  public static Stock create(Item item){
    return Stock.builder()
        .item(item)
        .qty(BigDecimal.ZERO)
        .build();

  }

  public void setQty(BigDecimal qty){
    this.qty = qty;
  }

  public void setCurrentQty(BigDecimal qty){
    this.qty = qty;
  }

  public void setLotNumber(LotNumber lotNumber){
    this.lotNumber = lotNumber;
  }
}
