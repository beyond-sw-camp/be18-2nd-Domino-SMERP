package com.domino.smerp.stockmovement.service;

import com.domino.smerp.item.Item;
import com.domino.smerp.stockmovement.StockMovement;
import com.domino.smerp.stockmovement.constants.TransactionType;
import com.domino.smerp.stockmovement.dto.request.CreateStockMovementRequest;
import com.domino.smerp.stockmovement.dto.request.UpdateStockMovementRequest;
import com.domino.smerp.stockmovement.dto.response.StockMovementListResponse;
import com.domino.smerp.stockmovement.dto.response.StockMovementResponse;
import com.domino.smerp.user.User;
import com.domino.smerp.workorder.WorkOrder;
import java.math.BigDecimal;
import java.util.List;

public interface StockMovementService {
  StockMovementListResponse getAllStockMovements();

  void createStockMovement(CreateStockMovementRequest createStockMovementRequest);

  List<StockMovement> createInboundStockMovement(Item item, BigDecimal movedQty,
      User user, TransactionType transactionType, String documentNo, BigDecimal totalQty);

  List<StockMovement> createProduceStockMovement(WorkOrder workOrder);

  //void createOutboundStockMovement(BigDecimal movedQty, Item item, User user,
      //TransactionType transactionType, String documentNo);

  StockMovementResponse createAdjustStockMovement(UpdateStockMovementRequest updateStockMovementRequest);


}
