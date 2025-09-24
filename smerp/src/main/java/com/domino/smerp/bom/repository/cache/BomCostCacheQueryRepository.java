package com.domino.smerp.bom.repository.cache;

import com.domino.smerp.bom.dto.response.BomRequirementResponse;
import java.math.BigDecimal;
import java.util.List;

public interface BomCostCacheQueryRepository {
  List<BomRequirementResponse> findResponsesByRootItemId(final Long rootItemId);
  BigDecimal getTotalCost(final Long rootItemId);

}
