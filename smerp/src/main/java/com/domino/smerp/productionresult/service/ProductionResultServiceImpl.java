package com.domino.smerp.productionresult.service;

import com.domino.smerp.common.util.DocumentNoGenerator;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.repository.ItemRepository;
import com.domino.smerp.productionresult.ProductionResult;
import com.domino.smerp.productionresult.ProductionResultRepository;
import com.domino.smerp.productionresult.dto.request.CreateProductionResultRequest;
import com.domino.smerp.productionresult.dto.request.UpdateProductionResultRequest;
import com.domino.smerp.productionresult.dto.response.ProductionResultListResponse;
import com.domino.smerp.productionresult.dto.response.ProductionResultResponse;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.repository.WarehouseRepository;
import com.domino.smerp.workorder.WorkOrder;
import com.domino.smerp.workorder.repository.WorkOrderRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductionResultServiceImpl implements ProductionResultService {
  private final ProductionResultRepository productionResultRepository;
  private final WorkOrderRepository workOrderRepository;
  private final WarehouseRepository warehouseRepository;
  private final ItemRepository itemRepository;
  private final UserRepository userRepository;
  private final DocumentNoGenerator documentNoGenerator;
  //private final StockMovementService stockMovementService;

  @Override
  @Transactional(readOnly = true)
  //목록 조회
  public ProductionResultListResponse getAllProductionResults(){

    //빈 목록 가능 -> 예외 처리 x
    List<ProductionResult> productionResults = productionResultRepository.findByIsDeletedFalse();

    List<ProductionResultResponse> productionResultResponses = new ArrayList<>();

    BigDecimal totalQty = BigDecimal.ZERO;

    for(ProductionResult productionResult : productionResults){
      ProductionResultResponse productionResultResponse = toProductionResultResponse(productionResult);
      productionResultResponses.add(productionResultResponse);
      totalQty = totalQty.add(productionResultResponse.getQty());
    }

    ProductionResultListResponse productionResultListResponse = ProductionResultListResponse.builder()
        .productionResultResponses(productionResultResponses)
        .totalQty(totalQty)
        .build();

    return productionResultListResponse;

  }

  //상세 조회
  @Override
  @Transactional(readOnly = true)
  public ProductionResultResponse getProductionResultById(final Long id){

    ProductionResult productionResult = productionResultRepository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(() -> new EntityNotFoundException("production result not found by id"));

    //soft delete 된 거 제외
    return toProductionResultResponse(productionResult);
  }

  // 전표 생성
  @Transactional
  public String generateDocumentNoWithRetry(LocalDate documentDate) {
    return documentNoGenerator.generate(documentDate, productionResultRepository::findMaxSequenceByPrefix);
  }

  //생산실적 생성
  //생산실적 생성 시 complete된 work order만 보여줌 or 선택 work order complete 확인
  @Override
  @Transactional
  public ProductionResultResponse createProductionResult(final CreateProductionResultRequest createProductionResultRequest){

    WorkOrder workOrder = workOrderRepository.findById(createProductionResultRequest.getWorkOrderId())
        .orElseThrow(() -> new EntityNotFoundException("work order not found by id"));

    Warehouse departWarehouse = warehouseRepository.findByName(createProductionResultRequest.getFactoryName())
        .orElseThrow(() -> new EntityNotFoundException("warehouse not found by id"));

    Item item = itemRepository.findByName(createProductionResultRequest.getItemName())
        .orElseThrow(() -> new EntityNotFoundException("item not found by name"));


    User user = userRepository.findByName(createProductionResultRequest.getUserName())
        .orElseThrow(() -> new EntityNotFoundException("user not found by name"));


    //production result entity로 create을 넣으면 인자가 너무 많아짐
    //반복되는 경우 있을 때 create 생성하고 재활용

    //작업지시 > 재고 > 재고수불 > 생산실적
    //사용자 요청에 의해 만드는 경우
    //창고가 바로 만들어짐
    //생산 수량에 대해 가능한 위치를 지닌 창고를 선택했어야함

    String documentNo = generateDocumentNoWithRetry(LocalDate.now());


    ProductionResult productionResult = ProductionResult.builder()
        .documentNo(documentNo)
        .user(user)
        .departWarehouse(departWarehouse)
        .workOrder(workOrder)
        .item(item) //품목 코드, 품목명, 규격 생략
        .qty(createProductionResultRequest.getQty())
        .isDeleted(false)
        .build();
    productionResultRepository.save(productionResult);

    //production result의 work order을 바탕으로 재고 수불 반영
    //stockMovementService.createProduceStockMovement(workOrder);

    workOrder.setProducedAt(createProductionResultRequest.getProducedAt());
    workOrder.setProductionResult(productionResult);

    return toProductionResultResponse(productionResult);

  }


  @Override
  @Transactional
  //작업지시 status complete으로 수정 시 production result 생성됨
  public ProductionResult createProductionResultByWorkOrder(
      WorkOrder workOrder, BigDecimal producedQty){

    String documentNo = generateDocumentNoWithRetry(LocalDate.now());


    //생산결과 생성
    ProductionResult productionResult = ProductionResult.builder()
        .documentNo(documentNo)
        .user(workOrder.getProductionPlan().getUser())
        .departWarehouse(workOrder.getWarehouse())
        .workOrder(workOrder)
        .item(workOrder.getItem()) //품목 코드, 품목명, 규격 생략
        .qty(producedQty)
        .isDeleted(false)
        .build();

    workOrder.setProductionResult(productionResult);

    productionResultRepository.save(productionResult);
    return productionResult;
  }

  //수정
  @Override
  @Transactional
  public ProductionResultResponse updateProductionResult(
      final Long id,
      final UpdateProductionResultRequest updateProductionResultRequest
  ){

    ProductionResult productionResult = productionResultRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("production result not found by id"));

    ProductionResult updatedProductionResult = ProductionResult.builder()

        .id(productionResult.getId()) // 기존 ID 유지

        .user(updateProductionResultRequest.getUserName() != null
            ? userRepository.findByName(updateProductionResultRequest.getUserName())
            .orElseThrow(() -> new EntityNotFoundException("User not found by name"))
            : productionResult.getUser())

        .documentNo(updateProductionResultRequest.getDocumentNo() != null
            ? updateProductionResultRequest.getDocumentNo()
            : productionResult.getDocumentNo())

        .departWarehouse(updateProductionResultRequest.getFactoryName() != null
            ? warehouseRepository.findByName(updateProductionResultRequest.getFactoryName())
            .orElseThrow(() -> new EntityNotFoundException("Factory Warehouse not found by name"))
            : productionResult.getDepartWarehouse())

        .workOrder(updateProductionResultRequest.getWorkOrderId() != null
            ? workOrderRepository.findById(updateProductionResultRequest.getWorkOrderId())
            .orElseThrow(() -> new EntityNotFoundException("WorkOrder not found by name"))
            : productionResult.getWorkOrder())

        .item(updateProductionResultRequest.getItemName() != null
            ? itemRepository.findByName(updateProductionResultRequest.getItemName())
            .orElseThrow(() -> new EntityNotFoundException("Item not found by name"))
            : productionResult.getItem())

        .qty(updateProductionResultRequest.getQty() != null
            ? updateProductionResultRequest.getQty()
            : productionResult.getQty())

       .build();

    productionResultRepository.save(updatedProductionResult);

    WorkOrder workOrder = updatedProductionResult.getWorkOrder();
    workOrder.setProducedAt(updateProductionResultRequest.getProducedAt());
    workOrder.setProductionResult(updatedProductionResult);

    //수정시에도 재고 수불 -> 재고 가능

    return toProductionResultResponse(updatedProductionResult);
  }



  //soft delete
  @Override
  @Transactional
  public void softDeleteProductionResult(final Long id){
    ProductionResult productionResult = productionResultRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("work order id invalid"));

    productionResult.setIsDeleted(true);
    //7일 후 삭제

    //저장 - transactional로 자동 (id 찾음)
  }

  @Override
  @Transactional
  public void hardDeleteProductionResult(final Long id){
    ProductionResult productionResult = productionResultRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("work order not found by id"));
    if(!productionResult.isDeleted())
      throw new IllegalArgumentException("work order is not softly deleted");

    productionResultRepository.delete(productionResult);

  }

  private ProductionResultResponse toProductionResultResponse(ProductionResult productionResult){

    String remark = "";
    if(productionResult.getWorkOrder().getProductionPlan().getRemark() != null)
      remark = productionResult.getWorkOrder().getProductionPlan().getRemark();

    return ProductionResultResponse.builder()

        .id(productionResult.getId())

        // 일자 no
        .documentNo(productionResult.getDocumentNo())

        // 생산 공장
        .factoryName(productionResult.getDepartWarehouse().getName())

        //현 창고
        //.arriveWarehouseName(productionResult.getStockMovement().getWarehouse())

        // 품목명
        .itemName(productionResult.getItem().getName())

        //규격
        .specification(productionResult.getItem().getSpecification())

        // 수량
        .qty(productionResult.getQty())

        // 작업지시서 no
        .workOrderId(productionResult.getWorkOrder().getId())

        .remark(remark)

        .build();
  }


}
