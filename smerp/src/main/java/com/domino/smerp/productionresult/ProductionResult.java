package com.domino.smerp.productionresult;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.item.Item;
import com.domino.smerp.productionresult.dto.request.CreateProductionResultRequest;
import com.domino.smerp.productionresult.dto.request.UpdateProductionResultRequest;
import com.domino.smerp.user.User;
import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.workorder.WorkOrder;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "production_result")
@ToString(onlyExplicitlyIncluded = true)
public class ProductionResult extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pr_id")
  private Long id;

  //품목
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id",
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
      nullable = false
  )
  private Item item;

  //사용자
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id",
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private User user;

  //출발 창고
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "depart_warehouse_id",
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
      nullable = false
  )
  private Warehouse departWarehouse;

  //도착 창고 x

  //작업지시 - 생산계획 쪽에서 필요한 정보
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "wo_id",
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private WorkOrder workOrder;


  @Column(nullable = false, precision = 12, scale = 2)
  @ToString.Include
  @Builder.Default
  private BigDecimal qty = BigDecimal.ZERO;

  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  @ToString.Include
  private boolean isDeleted = false;

  @Column(name = "document_no") //nullable = false
  @ToString.Include
  private String documentNo;

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void setWorkOrder(WorkOrder workOrder){
    this.workOrder = workOrder;
    if (workOrder != null && workOrder.getProductionResult() != this) {
      workOrder.setProductionResult(this);
    }
  }



}
