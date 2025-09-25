  package com.domino.smerp.stockmovement;

  import com.domino.smerp.stockmovement.dto.request.UpdateStockMovementRequest;
  import com.domino.smerp.stockmovement.dto.response.StockMovementListResponse;
  import com.domino.smerp.stockmovement.dto.response.StockMovementResponse;
  import com.domino.smerp.stockmovement.service.StockMovementService;
  import lombok.RequiredArgsConstructor;
  import org.springframework.http.ResponseEntity;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.PatchMapping;
  import org.springframework.web.bind.annotation.PathVariable;
  import org.springframework.web.bind.annotation.RequestBody;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.RestController;
  //재고 수불은 조회만
  @RestController
  @RequiredArgsConstructor
  @RequestMapping("/api/v1/stock-movements")
  public class StockMovementController {

    private final StockMovementService stockMovementService;

    //목록 조회
    @GetMapping
    public ResponseEntity<StockMovementListResponse> getAllStockMovements(){
      return ResponseEntity.ok().body(stockMovementService.getAllStockMovements());
    }

    //수정 - 재고 수불을 활용해 품목의 전체 재고량을 조정
    @PatchMapping("/{stock-movement-id}")
    public ResponseEntity<StockMovementResponse> getStockMovementById(
        @PathVariable("stock-movement-id") Long stockMovementId,
        @RequestBody UpdateStockMovementRequest updateStockMovementRequest){
      return ResponseEntity.ok().body(stockMovementService.createAdjustStockMovement(updateStockMovementRequest));
    }
  }
