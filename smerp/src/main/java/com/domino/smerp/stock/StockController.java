package com.domino.smerp.stock;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.domino.smerp.stock.dto.request.UpdateStockRequest;
import com.domino.smerp.stock.dto.response.StockListResponse;
import com.domino.smerp.stock.dto.response.StockResponse;
import com.domino.smerp.stock.service.StockService;

//재고는 수정, 조회만
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stocks")
public class StockController {
  private final StockService stockService;

  @GetMapping
  public ResponseEntity<StockListResponse> getAllStocks() {
    return ResponseEntity.ok().body(stockService.getAllStocks());
  }

  @GetMapping("/{stock-id}")
  public ResponseEntity<StockResponse> getStockById(@RequestParam("stock-id") Long stockId) {
    return ResponseEntity.ok().body(stockService.getStockById(stockId));
  }

  //test 용
  @PostMapping
  public ResponseEntity<StockResponse> createStock(@RequestParam Long itemId,
      @RequestParam BigDecimal qty) {
    stockService.allocateStock(itemId, qty);
    return ResponseEntity.status(201).build();
  }


  //test 용
  @PatchMapping
  public ResponseEntity<StockResponse> deleteStock(
      @RequestParam Long itemId,
      @RequestParam BigDecimal qty,
      @RequestParam String username
  ) {
    stockService.removeStock(itemId, qty, username);
    return ResponseEntity.status(204).build();
  }
//  @PatchMapping("/{stock-id}")
//  public ResponseEntity<StockResponse> updateStock(
//      @PathVariable("stock-id") Long stockId,
//      @RequestBody UpdateStockRequest updateStockRequest) {
//    return ResponseEntity.ok().body(stockService.updateStock(stockId, updateStockRequest));
//  }

}
