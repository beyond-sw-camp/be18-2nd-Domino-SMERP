package com.domino.smerp.productionplan;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.lotno.dto.request.SearchLotNumberRequest;
import com.domino.smerp.lotno.dto.response.LotNumberListResponse;
import com.domino.smerp.productionplan.dto.request.CreateProductionPlanRequest;
import com.domino.smerp.productionplan.dto.request.SearchProductionPlanRequest;
import com.domino.smerp.productionplan.dto.request.UpdateProductionPlanRequest;
import com.domino.smerp.productionplan.dto.response.CurrentProductionPlanListResponse;
import com.domino.smerp.productionplan.dto.response.ProductionPlanListResponse;
import com.domino.smerp.productionplan.dto.response.ProductionPlanResponse;
import com.domino.smerp.productionplan.service.ProductionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plans")
public class ProductionPlanController {

  private final ProductionPlanService productionPlanService;

  //목록 조회
  @GetMapping
  public ResponseEntity<ProductionPlanListResponse> getAllProductionPlans() {
    ProductionPlanListResponse productionPlanListResponse = productionPlanService.getAllProductionPlans();
    return ResponseEntity.status(200).body(productionPlanListResponse);
  }

  //검색
  @GetMapping("/search")
  public ResponseEntity<PageResponse<CurrentProductionPlanListResponse>> getLotNumbers(
      final @ModelAttribute SearchProductionPlanRequest keyword,
      final Pageable pageable) {
    return ResponseEntity.ok(productionPlanService.searchProductionPlans(keyword, pageable));
  }


  //상세 조회
  @GetMapping("/{plan-id}")
  public ResponseEntity<ProductionPlanResponse> getProductionPlan(
      @PathVariable("plan-id") Long planId) {
    ProductionPlanResponse productionPlanResponse = productionPlanService.getProductionPlan(planId);
    return ResponseEntity.status(200).body(productionPlanResponse);
  }

  //생성
  @PostMapping
  public ResponseEntity<ProductionPlanResponse> createProductionPlan(
      @Valid @RequestBody CreateProductionPlanRequest createProductionPlanRequest) {
    ProductionPlanResponse productionPlanResponse = productionPlanService.createProductionPlan(
        createProductionPlanRequest);
    return ResponseEntity.status(201).body(productionPlanResponse);
  }

  //수정
  @PatchMapping("/{plan-id}")
  public ResponseEntity<ProductionPlanResponse> updateProductionPlan(
      @PathVariable("plan-id") Long planId,
      @Valid @RequestBody UpdateProductionPlanRequest updateProductionPlanRequest) {
    ProductionPlanResponse productionPlanResponse = productionPlanService.updateProductionPlan(
        planId, updateProductionPlanRequest);
    return ResponseEntity.status(200).body(productionPlanResponse);
  }

  //삭제(soft delete) - hard delete x, 취소는 로그에서
  @DeleteMapping("/{plan-id}")
  public ResponseEntity deleteProductionPlan(@PathVariable("plan-id") Long planId) {
    productionPlanService.softDeleteProductionPlan(planId);
    return ResponseEntity.status(204).build();
  }


}
