package com.domino.smerp.productionplan.service;

import com.domino.smerp.productionplan.ProductionPlan;
import com.domino.smerp.productionplan.dto.CreateProductionPlanRequest;
import com.domino.smerp.productionplan.dto.UpdateProductionPlanRequest;
import com.domino.smerp.productionplan.dto.response.ProductionPlanListResponse;
import com.domino.smerp.productionplan.dto.response.ProductionPlanResponse;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface ProductionPlanService {

  ProductionPlanListResponse getAllProductionPlans();

  ProductionPlanResponse getProductionPlan(final Long id);

  //사용자의 실제 생성
  ProductionPlanResponse createProductionPlan(final CreateProductionPlanRequest createProductionPlanRequest);

  //주문 품목에 의해 생성
  //List<ProductionPlan> createProductionPlansByItemOrder(Long itemOrderId);

  //void checkAvailablePlanForWorkOrder(Long itemId);

  //안전재고 이하 시 생성
  //List<ProductionPlan> createProductionPlansForSafetyStock(Long itemId);

  //사용자의 수정
  @Transactional
  ProductionPlanResponse updateProductionPlan(Long id,
      UpdateProductionPlanRequest updateProductionPlanRequest);

  void softDeleteProductionPlan(final Long id);

  void hardDeleteProductionPlan(final Long id);

}
