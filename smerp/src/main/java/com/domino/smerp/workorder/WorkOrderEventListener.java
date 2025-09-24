//package com.domino.smerp.workorder;
//
//import com.domino.smerp.productionplan.event.BOMReadyEvent;
//import com.domino.smerp.workorder.stock.StockAboveItemOrderQtyEvent;
//import com.domino.smerp.workorder.service.WorkOrderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.event.TransactionalEventListener;
//
//@Component
//@RequiredArgsConstructor
//public class WorkOrderEventListener {
//
//  private final WorkOrderService workOrderService;
//
//  //bom 가능
//  @TransactionalEventListener //commit 후
//  public void onBOMItemReady(final BOMReadyEvent event){
//    workOrderService.createWorkOrderIfAvailable(event.getProductionPlanId());
//  }
//
//  //주문 품목 수량 가능
//  @TransactionalEventListener
//  public void onItemOrderQtyReady(final StockAboveItemOrderQtyEvent event){
//    workOrderService.createWorkOrderIfAvailable(event.getProductionPlanId());
//  }
//
//
//}
