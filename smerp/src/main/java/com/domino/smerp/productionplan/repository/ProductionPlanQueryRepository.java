package com.domino.smerp.productionplan.repository;

import com.domino.smerp.productionplan.ProductionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductionPlanQueryRepository {

 Page<ProductionPlan> searchProductionPlans(
      final com.domino.smerp.productionplan.dto.request.SearchProductionPlanRequest keyword,
      final Pageable pageable);
}
