package com.domino.smerp.location;

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
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "location")
public class Location {

  @Id
  @Column(name = "location_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //RFID-W01-R06-L02-B07
  @Column(name = "bin_rfid", nullable = false)
  private String binRfid;

  //R01-20
  @Column(name = "rack_no", nullable = false)
  private String rackNo;

  //L01-05
  @Column(name = "level_no", nullable = false)
  private String levelNo;

  //B01-30
  @Column(name = "bin_no", nullable = false)
  private String binNo;

  @Column(nullable = false)
  @Builder.Default
  private boolean filled = false;

  @Column(nullable = false, precision = 12, scale = 2)
  @Builder.Default
  private BigDecimal maxQty = new BigDecimal("500.00");

  @Column(nullable = false)
  @Builder.Default
  private BigDecimal curQty = BigDecimal.ZERO;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "warehouse_id",
      nullable = false,
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private Warehouse warehouse;

  public static Location create(String rackNo, String levelNo, String binNo, String binRfid,
      Warehouse warehouse) {

    return Location.builder()
        .rackNo(rackNo)
        .levelNo(levelNo)
        .binNo(binNo)
        .binRfid(binRfid)
        .filled(false)
        .warehouse(warehouse)
        .build();
  }

  public void setFilled(boolean filled) {
    this.filled = filled;
  }

  //warehouse에서 사용
  public void setWarehouse(Warehouse warehouse){
    this.warehouse = warehouse;

    if(warehouse != null && !warehouse.getLocations().contains(this)){
      warehouse.getLocations().add(this);
    }

  }

  public void setCurQty(BigDecimal curQty) {
    this.curQty = curQty;
  }

}
