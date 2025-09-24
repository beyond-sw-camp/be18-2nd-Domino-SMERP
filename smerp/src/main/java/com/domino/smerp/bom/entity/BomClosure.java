package com.domino.smerp.bom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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
@Table(name = "bom_closure", indexes = {
    @Index(name = "idx_bom_closure_ancestor", columnList = "ancestor_item_id"),
    @Index(name = "idx_bom_closure_descendant", columnList = "descendant_item_id")
})
public class BomClosure {

  @EmbeddedId
  private BomClosureId id;

  @Column(name = "depth", nullable = false)
  private Integer depth;

  public static BomClosure create(final Long ancestorItemId, final Long descendantItemId,
      final Integer depth) {
    return BomClosure.builder()
        .id(new BomClosureId(ancestorItemId, descendantItemId))
        .depth(depth)
        .build();
  }

  // 편의 메서드: 복합키의 각 부분을 쉽게 접근
  public Long getAncestorItemId() {
    return this.id.getAncestorItemId();
  }

  public Long getDescendantItemId() {
    return this.id.getDescendantItemId();
  }
}