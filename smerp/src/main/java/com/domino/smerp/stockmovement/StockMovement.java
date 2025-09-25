package com.domino.smerp.stockmovement;

import com.domino.smerp.common.BaseEntity;
import com.domino.smerp.lotno.LotNumber;
import com.domino.smerp.productionresult.ProductionResult;
import com.domino.smerp.stockmovement.constants.SrcDocType;
import com.domino.smerp.stockmovement.constants.TransactionType;
import com.domino.smerp.user.User;
import com.domino.smerp.warehouse.Warehouse;
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
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString(onlyExplicitlyIncluded = true)
public class StockMovement extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sm_id")
  @ToString.Include
  private Long id;

  //창고는 inbound, outbound 등 고려시 null 가능
  //출발 창고
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "depart_warehouse_id",
    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private Warehouse departWarehouse;

  //도착 창고
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "arrive_warehouse_id",
    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private Warehouse arriveWarehouse;

  //사용자 - 거래처
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id",
    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private User user;

  //lot no - 주문, 판매 등은 null 가능
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lot_id",
    foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private LotNumber lotNumber;

  //생산 실적
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pr_id",
      foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
  )
  private ProductionResult productionResult;

  //이동 유형
  @Enumerated(EnumType.STRING)
  @Column(name = "transaction_type", nullable = false)
  @ToString.Include
  private TransactionType transactionType;

  //참조 문서 유형
  @Enumerated(EnumType.STRING)
  @Column(name = "src_doc_type")
  @ToString.Include
  private SrcDocType srcDocType;

  //참조 문서 번호
  @Column(name = "src_doc_no")
  @ToString.Include
  private String srcDocNo;

  //이동 대상 수량
  @ToString.Include
  @Column(name = "moved_qty", precision = 12, scale = 2)
  @Builder.Default
  private BigDecimal movedQty = BigDecimal.ZERO;


  //수불 시점의 재고 수량
  @ToString.Include
  @Column(name = "total_qty", precision = 12, scale = 2)
  @Builder.Default
  private BigDecimal totalQty = BigDecimal.ZERO;

}
