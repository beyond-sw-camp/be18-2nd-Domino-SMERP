package com.domino.smerp.productionplan;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.itemorder.ItemOrder;
import com.domino.smerp.productionplan.constants.Status;
import com.domino.smerp.user.User;
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
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "production_plan")
@ToString(exclude = {"user", "itemOrder"})
public class ProductionPlan extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "pp_id")
  private Long id;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Status status = Status.PENDING;

  private String remark;

  private String title;

  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  private boolean isDeleted = false;

  @Column(name = "document_no")
  private String documentNo;

  @Column(precision = 12, scale = 3)
  @Builder.Default
  private BigDecimal qty = BigDecimal.ZERO;

  //user
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "user_id",
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private User user;

  //주문 품목(교차테이블)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_order_id",
    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private ItemOrder itemOrder;


  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void setStatus(Status status){
    this.status = status;
  }

  public void setUser(User user){
    this.user = user;
  }

  public void setRemark(String remark){
    this.remark = remark;
  }

  public void setQty(BigDecimal qty){ this.qty = qty; }
}
