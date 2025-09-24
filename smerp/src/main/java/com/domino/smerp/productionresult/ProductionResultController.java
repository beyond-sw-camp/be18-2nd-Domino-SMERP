package com.domino.smerp.productionresult;

import com.domino.smerp.productionresult.dto.request.UpdateProductionResultRequest;
import com.domino.smerp.productionresult.dto.response.ProductionResultListResponse;
import com.domino.smerp.productionresult.dto.response.ProductionResultResponse;
import com.domino.smerp.productionresult.service.ProductionResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/production-results")
public class ProductionResultController {

  private final ProductionResultService productionResultService;

  //목록 조회
  @GetMapping
  public ResponseEntity<ProductionResultListResponse> getProductionResults(){
    return ResponseEntity.status(200).body(productionResultService.getAllProductionResults());
  }

  //상세 조회
  @GetMapping("/{production-result-id}")
  public ResponseEntity<ProductionResultResponse> getProductionResult(
      @PathVariable("production-result-id") Long productionResultId){
    return ResponseEntity.status(200).body(productionResultService.getProductionResultById(productionResultId));
  }
//
//  //생성
//  @GetMapping
//  public ResponseEntity<ProductionResultResponse> getProductionResult(
//      @RequestBody CreateProductionResultRequest createProductionResultRequest){
//    return ResponseEntity.status(200).body(productionResultService.createProductionResult(createProductionResultRequest));
//  }
//
  //수정
  @PatchMapping("/{production-result-id}")
  public ResponseEntity<ProductionResultResponse> getProductionResult(
      @PathVariable("production-result-id") Long productionResultId,
      @RequestBody UpdateProductionResultRequest updateProductionResultRequest){
    return ResponseEntity.ok().body(productionResultService.updateProductionResult(
        productionResultId, updateProductionResultRequest)
    );
  }

  //soft delete
  @DeleteMapping("/{production-result-id}")
  public ResponseEntity deleteProductionResult(@PathVariable("production-result-id") Long productionResultId){
    productionResultService.softDeleteProductionResult(productionResultId);
    return ResponseEntity.status(204).build();
  }

}
