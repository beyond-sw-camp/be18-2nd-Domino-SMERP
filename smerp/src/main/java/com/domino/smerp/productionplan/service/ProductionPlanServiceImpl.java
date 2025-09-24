package com.domino.smerp.productionplan.service;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.common.util.DocumentNoGenerator;
import com.domino.smerp.item.repository.ItemRepository;
import com.domino.smerp.productionplan.ProductionPlan;
import com.domino.smerp.productionplan.dto.request.SearchProductionPlanRequest;
import com.domino.smerp.productionplan.dto.response.CurrentProductionPlanListResponse;
import com.domino.smerp.productionplan.repository.ProductionPlanRepository;
import com.domino.smerp.productionplan.constants.Status;
import com.domino.smerp.productionplan.dto.request.CreateProductionPlanRequest;
import com.domino.smerp.productionplan.dto.request.UpdateProductionPlanRequest;
import com.domino.smerp.productionplan.dto.response.ProductionPlanListResponse;
import com.domino.smerp.productionplan.dto.response.ProductionPlanResponse;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import com.domino.smerp.warehouse.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//예외처리

@Service
@RequiredArgsConstructor
public class ProductionPlanServiceImpl implements ProductionPlanService {

  private final ProductionPlanRepository productionPlanRepository;
  private final ItemRepository itemRepository;
  private final UserRepository userRepository;
  //private final StockRepository stockRepository;
  private final WarehouseRepository warehouseRepository;
  //private final WorkOrderService workOrderService;
  //private final StockService stockService;
  private final ApplicationEventPublisher eventPublisher;
  private final DocumentNoGenerator documentNoGenerator;

  @Override
  @Transactional(readOnly = true)
  public ProductionPlanListResponse getAllProductionPlans() {

    //soft delete 고려 - 안삭제된 경우만 조회
    List<ProductionPlanResponse> productionPlanResponses = new ArrayList<>();

    productionPlanRepository.findByIsDeletedFalse().forEach(productionPlan -> {
      productionPlanResponses.add(toProductionPlanResponse(productionPlan));
    });

    return ProductionPlanListResponse.builder()
        .productionPlans(productionPlanResponses)
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public ProductionPlanResponse getProductionPlan(final Long id) {

    //id 에 대해 없는 경우 예외 (soft delete 고려)
    ProductionPlan productionPlan = productionPlanRepository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(() -> new EntityNotFoundException("No production plan of id"));

    return toProductionPlanResponse(productionPlan);
  }

  // ProductionPlanServiceImpl.java

  @Override
  @Transactional(readOnly = true)
  public PageResponse<CurrentProductionPlanListResponse> searchProductionPlans(
      final SearchProductionPlanRequest keyword,
      final Pageable pageable) {

    return PageResponse.from(
        productionPlanRepository
            .searchProductionPlans(keyword, pageable)
            .map(CurrentProductionPlanListResponse::fromEntity)
    );
  }


  // 전표 생성
  @Transactional
  public String generateDocumentNoWithRetry(LocalDate documentDate) {
    return documentNoGenerator.generate(documentDate, productionPlanRepository::findMaxSequenceByPrefix);
  }

  //사용자에 의한 생성
  @Override
  @Transactional
  public ProductionPlanResponse createProductionPlan(
      final CreateProductionPlanRequest createProductionPlanRequest) {

    //매니저인지 확인 -> 아니라면 권한 없으므로 생성 불가

    //title 유일(soft delete 된 것 포함)
    if (productionPlanRepository.existsByTitle(createProductionPlanRequest.getTitle())) {
      throw new IllegalArgumentException("Production Plan title duplicated");
    }

    User user = userRepository.findByName(createProductionPlanRequest.getName())
        .orElse(null);

    String documentNo = generateDocumentNoWithRetry(LocalDate.now());

    ProductionPlan productionPlan = ProductionPlan.builder()
        .status(Status.PENDING)
        .remark(createProductionPlanRequest.getRemark())
        .title(createProductionPlanRequest.getTitle())
        .isDeleted(false)
        .documentNo(documentNo)
        //.itemOrder(null)
        .qty(createProductionPlanRequest.getQty())
        .user(user)
        .build();

    productionPlanRepository.save(productionPlan);

    return toProductionPlanResponse(productionPlan);
  }

  /*
  //주문 품목 생성에 대한 생산계획 자동 생성
  //1 주문 품목에 대해 여러 생산계획 가능 (1:n)
  //생산하는 수량에 대한 공장의 생산량 기준 분리
  @Override
  @Transactional
  public List<ProductionPlan> createProductionPlansByItemOrder(Long itemOrderId){

    ItemOrder itemOrder = itemOrderRepository.findById(orderItemId)
        .orElseThrow(() -> new EntityNotFoundException("no order item of id"));

    BigDecimal requiredQty = itemOrder.getQty();

    List<ProductionPlan> productionPlans = splitPlansByCapacity(requiredQty, itemOrder);

    productionPlanRepository.saveAll(productionPlans);
    return productionPlans;
  }


   */
  /*
  @Override
  @Transactional(readOnly = true)
  //안전재고 이상일 때 가능한 수량 있는지 확인
  public void checkAvailablePlanForWorkOrder(Long itemId){

    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new EntityNotFoundException("item not found by id"));

    BigDecimal totalQty = stockService.getTotalStock(itemId);

    List<ProductionPlan> pendingProductionPlans = productionPlanRepository.findByStatusAndItemId(
        Status.PENDING, itemId
    );

    for(ProductionPlan productionPlan : pendingProductionPlans){
      if(productionPlan.getQty().compareTo(totalQty) <= 0){ //plan <= total -> 가능

        //work order과 분리하기 위해 event listener로
        eventPublisher.publishEvent(
            new StockAboveItemOrderQtyEvent(productionPlan.getId())
        );
      }
    }


  }

   */
/*
  //안전재고 없는 경우 생산계획 생성
  @Override
  @Transactional
  public List<ProductionPlan> createProductionPlansForSafetyStock(Long itemId){

    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new EntityNotFoundException("item not found by id"));


    //안전재고수량보다 부족한 수량만큼 생산 필요(총 재고 수량에 대해 비교 필요)
    BigDecimal requiredQty = item.getSafetyStock()
        .subtract(stockService.getTotalStock(item.getItemId()))
        .max(BigDecimal.ZERO);


    List<ProductionPlan> productionPlans = splitPlansByCapacity(requiredQty, null);
    productionPlanRepository.saveAll(productionPlans);
    return productionPlans;

  }

*/
  /*


  //생산능력 바탕으로 생산계획 필요한만큼 나누기
  public List<ProductionPlan> splitPlansByCapacity(BigDecimal remainQty, @Nullable ItemOrder itemOrder){

    int dailyCapacity = 1600; //가정

    BigDecimal totalQty = remainQty;

    List<ProductionPlan> productionPlans = new ArrayList<>();


    while(totalQty.compareTo(BigDecimal.ZERO) > 0){

      //생산가능양 > 생산필요양 -> 생산필요양 기준으로 생산
      //생산가능양 < 생산필요양 -> 생산가능양 기준 생산
      BigDecimal planQty = totalQty.min(BigDecimal.valueOf(dailyCapacity));

      ProductionPlan productionPlan = ProductionPlan.builder()
          .user(null)
          .status(Status.PENDING)
          .remark(null)
          .title(null)
          .isDeleted(false)
          //.documentNo(documentNo)
          //.itemOrder(itemOrder)
          .qty(planQty)
          .build();

      productionPlans.add(productionPlan); //필요한만큼 생산계획 생성

      totalQty = totalQty.subtract(planQty); //생산예정 수량만큼 제외

    }

    return productionPlans;
  }
*/


  //사용자의 수정
  @Transactional
  @Override
  public ProductionPlanResponse updateProductionPlan(final Long id,
      final UpdateProductionPlanRequest updateProductionPlanRequest) {

    ProductionPlan productionPlan = productionPlanRepository.getById(id);


    //isDeleted false인 경우 - true인 경우 삭제에 대한 취소 불가
    if (productionPlan.isDeleted()) {
      throw new IllegalArgumentException("Production Plan is deleted");
    }

    //title 유일 - soft delete의 복원가능성 있어서 배제 x
    if (productionPlanRepository.existsByTitle(updateProductionPlanRequest.getTitle())
        && !updateProductionPlanRequest.getTitle().equals(productionPlan.getTitle())) {
      throw new IllegalArgumentException("Production Plan title duplicated");
    }

    User user = updateProductionPlanRequest.getName() != null?
    userRepository.findByName(updateProductionPlanRequest.getName())
        .orElseThrow(() -> new EntityNotFoundException("User not found"))
            : productionPlan.getUser();

    ProductionPlan updatedProductionPlan = ProductionPlan.builder()
        .id(productionPlan.getId()) // 기존 ID 유지
        .documentNo(productionPlan.getDocumentNo())
        .title(updateProductionPlanRequest.getTitle() != null ? updateProductionPlanRequest.getTitle() : productionPlan.getTitle())
        .qty(updateProductionPlanRequest.getQty() != null ? updateProductionPlanRequest.getQty() : productionPlan.getQty())
        .remark(updateProductionPlanRequest.getRemark() != null ? updateProductionPlanRequest.getRemark() : productionPlan.getRemark())
        .status(updateProductionPlanRequest.getStatus() != null ? updateProductionPlanRequest.getStatus() : productionPlan.getStatus())
        .user(user)
        .build();

    productionPlanRepository.save(updatedProductionPlan);

    return toProductionPlanResponse(updatedProductionPlan);

  }

  @Override
  @Transactional
  public void softDeleteProductionPlan(final Long id) {

    //id 없는 경우 예외
    ProductionPlan productionPlan = productionPlanRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("No production plan by id"));
    productionPlan.setIsDeleted(true);

    //시간 설정해서 7일 후 삭제 (updated 날짜 기준)

    productionPlanRepository.save(productionPlan);
  }

  public void hardDeleteProductionPlan(final Long id){
    ProductionPlan productionPlan = productionPlanRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("No production plan by id"));

    if(!productionPlan.isDeleted())
      throw new IllegalArgumentException("production plan is not softly deleted");

    //실제 삭제
    productionPlanRepository.delete(productionPlan);

  }

  public ProductionPlanResponse toProductionPlanResponse(final ProductionPlan productionPlan) {
    return ProductionPlanResponse.builder()
        .id(productionPlan.getId())
        .name(Optional.ofNullable(productionPlan.getUser())
        .map(User::getName)
        .orElse(null))
        .qty(productionPlan.getQty())
        .status(productionPlan.getStatus())
        .remark(productionPlan.getRemark())
        .title(productionPlan.getTitle())
        .documentNo(productionPlan.getDocumentNo())
        .isDeleted(productionPlan.isDeleted()) //getIsDeleted x
        .build();
  }
}
