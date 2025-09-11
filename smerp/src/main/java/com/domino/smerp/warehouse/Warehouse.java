package com.domino.smerp.warehouse;

//import com.domino.smerp.location.Location;
import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.warehouse.constants.DivisionType;
import com.domino.smerp.warehouse.dto.WarehouseRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@Table(name = "warehouse")
public class Warehouse extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "warehouse_id")
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(name = "division_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private DivisionType divisionType;

  @Column(nullable = false)
  private boolean active;

  @Column(nullable = false)
  private String address;

  @Column(nullable = false)
  private String zipcode;

  /*

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "warehouse", orphanRemoval = true)
  @Builder.Default
  private List<Location> locations = new ArrayList<>();

  */
  //Boolean - null, false, true
  public void update(WarehouseRequest warehouseRequest) {

    if (warehouseRequest.getName() != null) {
      this.name = warehouseRequest.getName();
    }

    if (warehouseRequest.getDivisionType() != null) {
      this.divisionType = warehouseRequest.getDivisionType();
    }

    if (warehouseRequest.getActive() != null) { //null 시 기존대로 유지
      this.active = warehouseRequest.getActive();
    }

    if (warehouseRequest.getAddress() != null) {
      this.address = warehouseRequest.getAddress();
    }

    if (warehouseRequest.getZipcode() != null) {
      this.zipcode = warehouseRequest.getZipcode();
    }

  }

  public static Warehouse create(WarehouseRequest warehouseRequest) {

    return Warehouse.builder()
        .name(warehouseRequest.getName())
        .active(warehouseRequest.getActive())
        .address(warehouseRequest.getAddress())
        .zipcode(warehouseRequest.getZipcode())
        .divisionType(warehouseRequest.getDivisionType())
        .build();
  }
}
