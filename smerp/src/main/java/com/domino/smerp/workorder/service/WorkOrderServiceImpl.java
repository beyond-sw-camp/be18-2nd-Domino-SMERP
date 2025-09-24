package com.domino.smerp.workorder.service;

import com.domino.smerp.client.Client;
import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.common.util.DocumentNoGenerator;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.repository.ItemRepository;
import com.domino.smerp.location.service.LocationService;
import com.domino.smerp.productionplan.ProductionPlan;
import com.domino.smerp.productionplan.repository.ProductionPlanRepository;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.repository.WarehouseRepository;
import com.domino.smerp.workorder.WorkOrder;
import com.domino.smerp.workorder.dto.request.SearchWorkOrderRequest;
import com.domino.smerp.workorder.dto.response.SearchWorkOrderListResponse;
import com.domino.smerp.workorder.repository.WorkOrderRepository;
import com.domino.smerp.workorder.constants.Status;
import com.domino.smerp.workorder.dto.request.CreateWorkOrderRequest;
import com.domino.smerp.workorder.dto.request.UpdateWorkOrderRequest;
import com.domino.smerp.workorder.dto.response.CurrentWorkOrderListResponse;
import com.domino.smerp.workorder.dto.response.CurrentWorkOrderResponse;
import com.domino.smerp.workorder.dto.response.WorkOrderListResponse;
import com.domino.smerp.workorder.dto.response.WorkOrderResponse;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService {

  private final WorkOrderRepository workOrderRepository;
  //private final BomService bomService;
  //private final StockService stockService;
  private final ProductionPlanRepository productionPlanRepository;
  //private final ProductionResultRepository productionResultRepository;
  private final ItemRepository itemRepository;
  private final WarehouseRepository warehouseRepository;
  private final LocationService locationService;
  //private final StockMovementService stockMovementService;
  private final UserRepository userRepository;
  //private final ProductionResultService productionResultService;
  private final DocumentNoGenerator documentNoGenerator;


  //목록 조회
  @Override
  @Transactional(readOnly = true)
  public WorkOrderListResponse getAllWorkOrders() {

    //soft delete 안된 대상만 조회
    List<WorkOrder> workOrders = workOrderRepository.findByIsDeletedFalse();

    List<WorkOrderResponse> workOrderResponses = new ArrayList<>();

    for(WorkOrder workOrder : workOrders) {
      workOrderResponses.add(toWorkOrderResponse(workOrder));
    }

    return WorkOrderListResponse.builder()
        .workOrderResponses(workOrderResponses)
        .build();

  }

  // 전표 생성
  @Transactional
  public String generateDocumentNoWithRetry(LocalDate documentDate) {
    return documentNoGenerator.generate(documentDate, workOrderRepository::findMaxSequenceByPrefix);
  }

  //상세 조회
  @Override
  @Transactional(readOnly = true)
  public WorkOrderResponse getWorkOrderById(final Long id) {

    //id 유효한데 soft delete 상태 x
    WorkOrder workOrder = workOrderRepository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(() -> new EntityNotFoundException("No work order of id"));

    return toWorkOrderResponse(workOrder);
  }

  @Transactional(readOnly = true)
  @Override
  public CurrentWorkOrderListResponse getAllCurrentWorkOrders(){
    //soft delete 안된 대상만 조회
    List<WorkOrder> workOrders = workOrderRepository.findByIsDeletedFalse();

    List<CurrentWorkOrderResponse> currentWorkOrderResponses = new ArrayList<>();

    for(WorkOrder workOrder : workOrders) {
      currentWorkOrderResponses.add(toCurrentWorkOrderResponse(workOrder));
    }

    return CurrentWorkOrderListResponse.builder()
        .currentWorkOrderResponses(currentWorkOrderResponses)
        .build();
  }

  //요청에 의한 생성
  @Override
  @Transactional
  public WorkOrderResponse createWorkOrder(final CreateWorkOrderRequest createWorkOrderRequest) {
    /*
    System.out.println(createWorkOrderRequest.getItemName());
    System.out.println(createWorkOrderRequest.getFactoryName());
    System.out.println(createWorkOrderRequest.getUserName());
    System.out.println(createWorkOrderRequest.getProductionPlanId());
    System.out.println(createWorkOrderRequest.getPlanQty());
    System.out.println(createWorkOrderRequest.getPlanAt());
    */
    Item item = itemRepository.findByName(createWorkOrderRequest.getItemName())
        .orElseThrow(() -> new EntityNotFoundException("item not found by name"));

    Warehouse warehouse = warehouseRepository.findByName(createWorkOrderRequest.getFactoryName())
        .orElseThrow(() -> new EntityNotFoundException("warehouse not found by name"));

    ProductionPlan productionPlan = productionPlanRepository.findById(createWorkOrderRequest.getProductionPlanId())
        .orElseThrow(() -> new EntityNotFoundException("production plan not found by id"));

    User user = userRepository.findByName(createWorkOrderRequest.getUserName())
        .orElseThrow(() -> new EntityNotFoundException("user not found by name"));

    String documentNo = generateDocumentNoWithRetry(LocalDate.now());


    //유일함 x
    WorkOrder workOrder = WorkOrder.builder()
        .item(item)
        .qty(createWorkOrderRequest.getPlanQty())
        .productionPlan(productionPlan) //실제로 필요
        .warehouse(warehouse)
        .status(Status.PENDING)
        .planAt(createWorkOrderRequest.getPlanAt())
        .producedAt(createWorkOrderRequest.getProducedAt())
        .documentNo(documentNo)
        .isDeleted(false)
        .build();

    workOrderRepository.save(workOrder);

    //work order 생성 시 생산계획 담당자, 적요 넣어야함
    //생산계획이 없음
    productionPlan.setUser(user);

    productionPlan.setRemark(createWorkOrderRequest.getRemark());

    return toWorkOrderResponse(workOrder);
  }
/*

  //work order 자동 생성용
  //plan에 대해 order에 의한 item이 있는 경우 가능
  //work order 없이 생산계획 직접 생성, 안전재고 수량에 의해 생성 시 불가
  //1. bom 가능
  //2. 주문 품목 수량만큼 가능
  //3. 품목 안전재고 수량보다 많음
  @Override
  @Transactional
    public void createWorkOrderIfAvailable(final Long planId){

    ProductionPlan productionPlan = productionPlanRepository.findById(planId)
        .orElseThrow(() -> new EntityNotFoundException("production plan not found by id"));

    Item item = itemRepository.findById(productionPlan.getItemOrder().getItem().getItemId())
        .orElseThrow(() -> new EntityNotFoundException("item not found by item order id"));

    //if(!bomService.hasBomItem(planId)) throw new IllegalStateException("production plan does not have bom item");
    if(!stockService.isAboveSafetyStock(item.getItemId())) throw new IllegalStateException("production plan does not have stock");
    if(!stockService.isAboveItemOrderQty(item.getItemId())) throw new IllegalStateException("production plan does not have stock");

    WorkOrder workOrder = WorkOrder.builder()
        .item(item)
        .productionPlan(productionPlan)
        .warehouse(null) //자동 생성이므로 지정 x -> 생산 실적 생성, 재고 수불 시 반영
        .status(com.domino.smerp.workorder.constants.Status.PENDING)
        .qty(BigDecimal.ZERO)
        .planAt(null) //자동 생성이므로 x
        .producedAt(null)
        .isDeleted(false)
        //.documentNo(documentNo)
        .build();

    workOrderRepository.save(workOrder);

  }




 */

  //검색
  @Override
  @Transactional(readOnly = true)
  public PageResponse<SearchWorkOrderListResponse> searchWorkOrders(
      final SearchWorkOrderRequest keyword,
      final Pageable pageable)
  {
    return PageResponse.from(
        workOrderRepository
            .searchWorkOrders(keyword, pageable)
            .map(SearchWorkOrderListResponse::fromEntity));
  }

  //수정 요청
  @Override
  @Transactional
  public WorkOrderResponse updateWorkOrder(final Long id,
      final UpdateWorkOrderRequest updateWorkOrderRequest) {


    //id 유효
    WorkOrder workOrder = workOrderRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("No work order of id: " + id));

    if(workOrder.getStatus().equals(Status.COMPLETED)) {
      throw new EntityNotFoundException("work order 작업 완료, 수정 불가합니다");
    }

    //유효성 체크 필요 x

    Item item = (updateWorkOrderRequest.getItemName() != null)
        ? itemRepository.findByName(updateWorkOrderRequest.getItemName())
        .orElseThrow(() -> new EntityNotFoundException("item not found by name"))
        : workOrder.getItem();

    Warehouse warehouse = (updateWorkOrderRequest.getFactoryName() != null)
        ? warehouseRepository.findByName(updateWorkOrderRequest.getFactoryName())
        .orElseThrow(() -> new EntityNotFoundException("warehouse not found by name"))
        : workOrder.getWarehouse();

    //production plan : update request의 plan 변경 시 반영 용도
    ProductionPlan productionPlan = (updateWorkOrderRequest.getProductionPlanId() != null)
        ? productionPlanRepository.findById(updateWorkOrderRequest.getProductionPlanId())
        .orElseThrow(() -> new EntityNotFoundException("production plan not found by id"))
        : workOrder.getProductionPlan();

    //user : update request의 username을 plan에 반영하는 용도
    User user = (updateWorkOrderRequest.getUserName() != null)
        ? userRepository.findByName(updateWorkOrderRequest.getUserName())
        .orElseThrow(() -> new EntityNotFoundException("user not found by name"))
        : productionPlan.getUser();


    WorkOrder updatedWorkOrder = WorkOrder.builder()
        .id(workOrder.getId())
        .item(item)
        .warehouse(warehouse)
        .productionPlan(productionPlan)
        .qty(
            updateWorkOrderRequest.getPlanQty() != null ?
                updateWorkOrderRequest.getPlanQty() : workOrder.getQty()
        )
        .documentNo(workOrder.getDocumentNo())
        .status(
            updateWorkOrderRequest.getStatus() != null ?
                updateWorkOrderRequest.getStatus() : workOrder.getStatus()
        )

        .planAt(
            updateWorkOrderRequest.getPlanAt() != null ?
                updateWorkOrderRequest.getPlanAt() : workOrder.getPlanAt()
        )

        .producedAt(updateWorkOrderRequest.getProducedAt() != null ?
            updateWorkOrderRequest.getProducedAt() : workOrder.getProducedAt()
        )

        .isDeleted(false)
        .build();

    //plan에 수정 반영될 부분
    productionPlan.setUser(user);
    productionPlan.setRemark(updateWorkOrderRequest.getRemark() != null ?
        updateWorkOrderRequest.getRemark() : productionPlan.getRemark());

    /*
    workOrderRepository.save(updatedWorkOrder);

    if(workOrder.getStatus() == Status.APPROVED) {

      productionResultService.createProductionResultByWorkOrder(workOrder);

      //stock movement 저장은 produce stock에서
      stockMovementService.createProduceStockMovement(workOrder);

      productionPlan.setStatus(com.domino.smerp.productionplan.constants.Status.COMPLETED);

    }

     */

    workOrderRepository.save(updatedWorkOrder);

    return toWorkOrderResponse(workOrder);
  }


  //soft delete
  @Override
  @Transactional
  public WorkOrderResponse softDelete(final Long id) {
    //id 유효, 이미 soft delete x
    //7일 후 삭제
    WorkOrder workOrder = workOrderRepository.findById(id)
        .orElseThrow(() -> new  EntityNotFoundException("No work order of id: "));

    workOrder.setIsDeleted(true);

    return toWorkOrderResponse(workOrder);
  }

  @Override
  @Transactional
  public void hardDeleteWorkOrder(final Long id){

    WorkOrder workOrder = workOrderRepository.findById(id)
        .orElseThrow(() -> new  EntityNotFoundException("No work order of id: "));

    if(!workOrder.isDeleted())
      throw new IllegalArgumentException("work order is not softly deleted");

    workOrderRepository.delete(workOrder);

  }

  public WorkOrderResponse toWorkOrderResponse(final WorkOrder workOrder) {

    BigDecimal producedQty = BigDecimal.ZERO;
    /*
    if (workOrder.getProductionResult() != null) {
      //생산실적 있는 경우 qty 반드시 있음
      producedQty = workOrder.getProductionResult().getQty();
    }

     */

    User user = workOrder.getProductionPlan().getUser(); //nullable
    String userName = (user != null) ? user.getName() : null;

    //user null인지 먼저 확인하고 user.getClient() 가능 유무를 확인해야함
    Client client = (user != null) ? user.getClient() : null; //nullable

    String companyName = (client != null) ? client.getCompanyName() : null;

    return WorkOrderResponse.builder()
        .id(workOrder.getId())
        .companyName(companyName)
        .userName(userName)
        .documentNo(workOrder.getDocumentNo())
        .itemName(workOrder.getItem().getName() != null ?
            workOrder.getItem().getName() : null) //null 가능
        .status(workOrder.getStatus())
        .planQty(workOrder.getQty())
        .producedQty(producedQty) //없다면 0
        .planAt(workOrder.getPlanAt() != null ?
            workOrder.getPlanAt() : null) //null 가능

        .build();
  }

  public CurrentWorkOrderResponse toCurrentWorkOrderResponse(final WorkOrder workOrder) {
    //BigDecimal producedQty = BigDecimal.ZERO;

    /*
    if (workOrder.getProductionResult() != null) {
      //생산실적 있는 경우 qty 반드시 있음
      producedQty = workOrder.getProductionResult().getQty();
    }

    */

    User user = workOrder.getProductionPlan().getUser(); //nullable
    String userName = (user != null) ? user.getName() : null;

    //user null인지 먼저 확인하고 user.getClient() 가능 유무를 확인해야함
    Client client = (user != null) ? user.getClient() : null; //nullable

    String companyName = (client != null) ? client.getCompanyName() : null;

    return CurrentWorkOrderResponse.builder()
        .id(workOrder.getId())
        .companyName(companyName)
        .userName(userName)
        .documentNo(workOrder.getDocumentNo())
        .itemName(workOrder.getItem().getName() != null ?
            workOrder.getItem().getName() : null) //null 가능
        .status(workOrder.getStatus())
        .planQty(workOrder.getQty())
        .planAt(workOrder.getPlanAt() != null ?
            workOrder.getPlanAt() : null) //null 가능
        .build();

  }

}
