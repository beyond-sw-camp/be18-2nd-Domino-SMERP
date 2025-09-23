package com.domino.smerp.workorder;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.item.Item;
import com.domino.smerp.productionplan.ProductionPlan;
import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.workorder.constants.Status;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
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
@Table(name = "work_order")
@ToString
public class WorkOrder extends BaseEntity {

  @Id
  @Column(name = "wo_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //창고
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "warehouse_id",
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private Warehouse warehouse;

  //품목 - 같은 품목애 대해 여러 작업지시 가능
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id",
    foreignKey =  @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private Item item;

  //생산계획
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pp_id",
    nullable = false,
    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private ProductionPlan productionPlan;

  /*
  //생산실적
  @OneToOne(mappedBy = "workOrder", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private ProductionResult productionResult;


   */

  @Column(nullable = false)
  @Builder.Default
  private Status status = Status.APPROVED;

  //지시 qty
  @Builder.Default
  private BigDecimal qty = BigDecimal.ZERO;

  @Column(name = "plan_at") //자동 생성 시
  private Instant planAt;

  @Column(name = "produced_at")
  private Instant producedAt;

  @Builder.Default
  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted = false;

  @Column(name = "document_no")
  private String documentNo;

  public void setProducedAt(Instant producedAt) {
    this.producedAt = producedAt;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  /*
  public void setProductionResult(ProductionResult productionResult) {
    this.productionResult = productionResult;
    if (productionResult != null) {
      productionResult.setWorkOrder(this);
    }
  }

   */
}
