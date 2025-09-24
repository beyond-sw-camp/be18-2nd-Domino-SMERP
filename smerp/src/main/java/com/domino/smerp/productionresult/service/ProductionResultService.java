package com.domino.smerp.productionresult.service;

import com.domino.smerp.productionresult.dto.request.CreateProductionResultRequest;
import com.domino.smerp.productionresult.dto.request.UpdateProductionResultRequest;
import com.domino.smerp.productionresult.dto.response.ProductionResultListResponse;
import com.domino.smerp.productionresult.dto.response.ProductionResultResponse;
import com.domino.smerp.workorder.WorkOrder;

public interface ProductionResultService {

  ProductionResultListResponse getAllProductionResults();

  ProductionResultResponse getProductionResultById(final Long id);

  ProductionResultResponse createProductionResult(final CreateProductionResultRequest createProductionResultRequest);

  void createProductionResultByWorkOrder(WorkOrder workOrder);

  ProductionResultResponse updateProductionResult(
      final Long id,
      final UpdateProductionResultRequest updateProductionResultRequest
  );

  void softDeleteProductionResult(final Long id);

  void hardDeleteProductionResult(final Long id);

}
