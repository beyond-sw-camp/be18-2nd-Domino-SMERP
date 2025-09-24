package com.domino.smerp.bom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

// 복합키용 클래스
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class BomClosureId implements Serializable {

  @Column(name = "ancestor_item_id")
  private Long ancestorItemId;

  @Column(name = "descendant_item_id")
  private Long descendantItemId;
}