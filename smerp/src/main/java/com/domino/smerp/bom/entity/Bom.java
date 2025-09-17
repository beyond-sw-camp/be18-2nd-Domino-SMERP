package com.domino.smerp.bom.entity;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.request.UpdateBomRequest;
import com.domino.smerp.item.Item;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "bom", indexes = {
    @Index(name = "idx_bom_parent_item", columnList = "parent_item_id"),
    @Index(name = "idx_bom_child_item", columnList = "child_item_id")})
public class Bom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "bom_id")
  private Long bomId;

  // 부모 품목
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_item_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private Item parentItem;

  // 자식 품목
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "child_item_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private Item childItem;

  @Column(name = "qty", nullable = false, precision = 12, scale = 3)
  private BigDecimal qty;

  @Column(name = "remark", columnDefinition = "TEXT")
  private String remark;


  public static Bom create(final CreateBomRequest request, final Item parentItem,
      final Item childItem) {
    return Bom.builder()
        .parentItem(parentItem)
        .childItem(childItem)
        .qty(request.getQty())
        .remark(request.getRemark())
        .build();
  }


  public void update(final UpdateBomRequest request) {
    if (request.getQty() != null) {
      this.qty = request.getQty();
    }
    if (request.getRemark() != null) {
      this.remark = request.getRemark();
    }
  }

  // update 메서드가 새로운 부모 품목 ID를 받아 관계를 수정하는 경우
  // `UpdateBomRequest`에 `newParentItemId` 필드가 있어야 합니다.
  public void updateRelation(final UpdateBomRequest request, final Item newParentItem) {
    if (newParentItem != null) {
      this.parentItem = newParentItem;
    }
    // 수량 및 비고도 함께 업데이트 가능
    this.update(request);
  }

}


