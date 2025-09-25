package com.domino.smerp.stock.service;

import com.domino.smerp.stock.Stock;
import com.domino.smerp.user.User;
import java.math.BigDecimal;
import com.domino.smerp.stock.dto.request.UpdateStockRequest;
import com.domino.smerp.stock.dto.response.StockListResponse;
import com.domino.smerp.stock.dto.response.StockResponse;
import java.util.List;

public interface StockService {

  //목록 조회
  StockListResponse getAllStocks();

  StockResponse getStockById(Long stockId);

  List<Stock> allocateStock(Long itemId, BigDecimal qty);

  List<Stock> removeStock(Long itemId, BigDecimal qty, User user);

  //alert까지 추상으로 두는게 맞나
  //일단 재고의 특성상 필수로 가져가긴 함
  void alertAboveSafetyStock(Long itemId);

  void alertBelowSafetyStock(Long itemId);

  //품목 총 재고 > 안전 재고
  boolean isAboveSafetyStock(Long itemId);

  //품목 총 재고 > 작업지시 수량
  boolean isAboveItemOrderQty(Long itemId);

  BigDecimal getTotalStock(Long itemId);

}
