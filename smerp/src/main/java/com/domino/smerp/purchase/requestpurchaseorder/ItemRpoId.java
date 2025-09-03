package com.domino.smerp.purchase.requestpurchaseorder;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ItemRpoId implements Serializable {

  @Column(name = "rpo_id", nullable = false)
  private Long rpoId;

  @Column(name = "item_id", nullable = false)
  private Long itemId;
}
