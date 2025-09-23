package com.domino.smerp.bom.dto.response;

import com.domino.smerp.bom.entity.BomCostCache;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BomRequirementResponse {

  private Long itemId;
  private String itemName;
  private String specification;
  private String unit;
  private String itemStatus;
  private BigDecimal requiredQty;  // targetQty 반영된 총 소요량
  private BigDecimal unitCost;
  private BigDecimal totalCost;
  private Integer depth;
}

