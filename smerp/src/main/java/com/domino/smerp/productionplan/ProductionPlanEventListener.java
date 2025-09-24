//package com.domino.smerp.productionplan;
//
//import com.domino.smerp.productionplan.event.ItemOrderCreatedEvent;
//import com.domino.smerp.productionplan.service.ProductionPlanService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.event.TransactionalEventListener;
//import stock.event.StockAboveSafetyEvent;
//import stock.event.StockBelowSafetyEvent;
//
//
//@Component
//@RequiredArgsConstructor
//public class ProductionPlanEventListener {
//
//  private final ProductionPlanService productionPlanService;
//
//  //주문 품목이 생성된 경우
//  @TransactionalEventListener
//  public void onItemOrderCreated(final ItemOrderCreatedEvent event){
//    productionPlanService.createProductionPlansByItemOrder(event.getItemOrderId());
//  }
//
//  //stock 부족 시 생산계획 생성
//  @TransactionalEventListener
//  public void stockBelowSafety(final StockBelowSafetyEvent event){
//    productionPlanService.createProductionPlansForSafetyStock(event.getItemId());
//  }
//
//  //stock
//  @TransactionalEventListener
//  public void stockAboveSafety(final StockAboveSafetyEvent event){
//    productionPlanService.checkAvailablePlanForWorkOrder(event.getItemId());
//  }
//
//
//
//
//}
