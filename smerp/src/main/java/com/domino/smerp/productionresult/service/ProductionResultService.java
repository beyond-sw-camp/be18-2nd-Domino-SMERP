package com.domino.smerp.productionresult.service;

import com.domino.smerp.productionresult.ProductionResult;
import com.domino.smerp.productionresult.dto.request.CreateProductionResultRequest;
import com.domino.smerp.productionresult.dto.request.UpdateProductionResultRequest;
import com.domino.smerp.productionresult.dto.response.ProductionResultListResponse;
import com.domino.smerp.productionresult.dto.response.ProductionResultResponse;
import com.domino.smerp.workorder.WorkOrder;
import java.math.BigDecimal;

public interface ProductionResultService {

  ProductionResultListResponse getAllProductionResults();

  ProductionResultResponse getProductionResultById(final Long id);

  ProductionResultResponse createProductionResult(final CreateProductionResultRequest createProductionResultRequest);

  ProductionResult createProductionResultByWorkOrder(WorkOrder workOrder, BigDecimal producedQty);

  ProductionResultResponse updateProductionResult(
      final Long id,
      final UpdateProductionResultRequest updateProductionResultRequest
  );

  void softDeleteProductionResult(final Long id);

  void hardDeleteProductionResult(final Long id);

}
