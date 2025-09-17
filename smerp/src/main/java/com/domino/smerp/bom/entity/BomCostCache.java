package com.domino.smerp.bom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
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
@Table(name = "bom_cost_cache", indexes = {
    @Index(name = "idx_cost_root", columnList = "root_item_id"),
    @Index(name = "idx_cost_child", columnList = "child_item_id")})
public class BomCostCache {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cache_id")
  private Long cacheId;

  @Column(name = "root_item_id", nullable = false)
  private Long rootItemId;

  @Column(name = "child_item_id", nullable = false)
  private Long childItemId;

  @Column(name = "depth", nullable = false)
  private Integer depth;

  @Column(name = "total_qty", nullable = false, precision = 12, scale = 3)
  private BigDecimal totalQty;

  @Column(name = "unit_cost", precision = 12, scale = 2)
  private BigDecimal unitCost;

  @Column(name = "total_cost", precision = 14, scale = 2)
  private BigDecimal totalCost;

  @Column(name = "last_at", nullable = false)
  private Instant lastAt;


  // 생성 메서드, update 메서드 등 비즈니스 로직은 서비스에서 구현
  public static BomCostCache create(final Long rootItemId, final Long childItemId,
      final Integer depth, final BigDecimal totalQty, final BigDecimal unitCost) {
    final BigDecimal safeQty = totalQty != null ? totalQty : BigDecimal.ZERO;
    final BigDecimal safeUnitCost = unitCost != null ? unitCost : BigDecimal.ZERO;
    final BigDecimal totalCost = safeQty.multiply(safeUnitCost);

    return BomCostCache.builder()
        .rootItemId(rootItemId)
        .childItemId(childItemId)
        .depth(depth)
        .totalQty(safeQty)
        .unitCost(safeUnitCost)
        .totalCost(totalCost)
        .lastAt(Instant.now())
        .build();
  }


}