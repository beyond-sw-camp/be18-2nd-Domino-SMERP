package com.domino.smerp.stockmovement.service;

import com.domino.smerp.client.Client;
import com.domino.smerp.item.Item;
import com.domino.smerp.item.repository.ItemRepository;
import com.domino.smerp.lotno.LotNumber;
import com.domino.smerp.lotno.LotNumberService;
import com.domino.smerp.stock.Stock;
import com.domino.smerp.stock.service.StockService;
import com.domino.smerp.stockmovement.StockMovement;
import com.domino.smerp.stockmovement.StockMovementRepository;
import com.domino.smerp.stockmovement.constants.SrcDocType;
import com.domino.smerp.stockmovement.constants.TransactionType;
import com.domino.smerp.stockmovement.dto.request.CreateStockMovementRequest;
import com.domino.smerp.stockmovement.dto.request.UpdateStockMovementRequest;
import com.domino.smerp.stockmovement.dto.response.StockMovementListResponse;
import com.domino.smerp.stockmovement.dto.response.StockMovementResponse;
import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import com.domino.smerp.workorder.WorkOrder;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl implements StockMovementService {

  private final StockMovementRepository stockMovementRepository;
  private final StockService stockService;
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;
  private final LotNumberService lotNumberService;

  //사용자용 api는 목록 조회만
  @Override
  @Transactional(readOnly = true)
  public StockMovementListResponse getAllStockMovements() {

    List<StockMovement> stockMovements = stockMovementRepository.findAll();

    List<StockMovementResponse> stockMovementResponses = new ArrayList<>();

    for (StockMovement stockMovement : stockMovements){
      StockMovementResponse stockMovementResponse = toStockMovementResponse(stockMovement);
      stockMovementResponses.add(stockMovementResponse);
    }

    return StockMovementListResponse.builder()
        .stockMovementResponses(stockMovementResponses)
        .build();
  }


  @Transactional
  @Override
  public void createStockMovement(CreateStockMovementRequest createStockMovementRequest) {

    Item item = itemRepository.findById(createStockMovementRequest.getItemId())
        .orElseThrow(() -> new EntityNotFoundException("item not found by id"));

    User user = userRepository.findById(createStockMovementRequest.getUserId())
        .orElseThrow(() -> new EntityNotFoundException("user not found by id"));

    //음수, 양수
    BigDecimal movedQty = createStockMovementRequest.getMovedQty();

    String documentNo = createStockMovementRequest.getDocumentNo();


    //함수 호출 이전 총 재고량
    BigDecimal totalQty = stockService.getTotalStock(item.getItemId());

    //당사가 구매를 한다, 반품당함 (-) => inbound
    if(movedQty.compareTo(BigDecimal.ZERO) < 0) {
      createInboundStockMovement(item, movedQty, user, createStockMovementRequest.getTransactionType(), documentNo, totalQty);
  }

    //판매를 한다, 폐기를 한다 (+) => outbound


    if(movedQty.compareTo(BigDecimal.ZERO) > 0) {
      createOutboundStockMovement(movedQty, item, user, createStockMovementRequest.getTransactionType(), documentNo);
    }
  }



  //구매를 한다, 반품을 한다 (- 로 받지만 실제로 창고에는 들어옴)
  public List<StockMovement> createInboundStockMovement(
      Item item, BigDecimal movedQty, User user, TransactionType transactionType, String documentNo, BigDecimal totalQty) {

    movedQty = movedQty.abs(); //(-) 로 받으므로 +가 됨

    List<Stock> stocks = stockService.allocateStock(
        item.getItemId(),
        movedQty
    );

    List<StockMovement> stockMovements = new ArrayList<>();

    SrcDocType srcDocType = transactionType == TransactionType.INBOUND
        ? SrcDocType.PURCHASE
        : SrcDocType.NONE;


    String srcDocNo = documentNo;
    if(srcDocType.equals(SrcDocType.PURCHASE))
        srcDocNo = documentNo + "PURCHASE";

    for(Stock stock : stocks){
      //inbound

      //재고 수불 생성
      StockMovement stockMovement = StockMovement.builder()
          .departWarehouse(null)
          .arriveWarehouse(stock.getLocation().getWarehouse())
          .user(user)
          .lotNumber(stock.getLotNumber())
          .movedQty(stock.getCurrentQty())
          .srcDocType(srcDocType)
          .srcDocNo(srcDocNo)
          .totalQty(stock.getQty())
          .transactionType(transactionType) //inbound or return
          .build();
      stockMovements.add(stockMovement);
      stockMovementRepository.save(stockMovement);
    }

    //안전재고 위라면 생산계획의 작업 지시 생성 가능 여부 확인
    stockService.alertAboveSafetyStock(item.getItemId());

    return stockMovements;
  }

  //재고 생산 실적용 -> 창고를 둘다 줘야함
  public List<StockMovement> createProduceStockMovement(WorkOrder workOrder){

    BigDecimal totalQty =  stockService.getTotalStock(workOrder.getItem().getItemId());

    BigDecimal runningTotalQty = totalQty;

    List<Stock> stocks = stockService.allocateStock(
        workOrder.getItem().getItemId(),
        workOrder.getQty()
    );

    List<StockMovement> stockMovements = new ArrayList<>();

//    User user = (workOrder.getProductionPlan().getUser() != null
//        ? workOrder.getProductionPlan().getUser() : null);

    //stock movement에는 production result, document no 필수임
    //document no 함수 받기 전이므로 null 처리
    String srcDocNo = (workOrder.getDocumentNo()) != null
        ? workOrder.getDocumentNo() + "PRODUCED"
        : "";

    for(Stock stock : stocks){

      runningTotalQty = runningTotalQty.add(stock.getQty());

      //재고 수불 생성
      StockMovement stockMovement = StockMovement.builder()
          .departWarehouse(workOrder.getWarehouse())
          .arriveWarehouse(stock.getLocation().getWarehouse())
          //.user(user)
          .lotNumber(stock.getLotNumber())
          .transactionType(TransactionType.TRANSFER)
          .movedQty(stock.getQty())
          //각 재고 반영시마다 total 구하고 추가
          .totalQty(runningTotalQty)
          .srcDocType(SrcDocType.PRODUCED)
          .srcDocNo(srcDocNo)
          .build();

      stockMovements.add(stockMovement);
      stockMovementRepository.save(stockMovement);
    }

    //안전재고 위라면 생산계획의 작업 지시 생성 가능 여부 확인
    stockService.alertAboveSafetyStock(workOrder.getItem().getItemId());

    return stockMovements;
  }


  //판매한다 (+)로 받음 -> 실제 재고 수불 (-)로 저장(빠져나가므로)
  //1. 판매
  //2.
  @Transactional
  public void createOutboundStockMovement(
      BigDecimal movedQty,
      Item item,
      User user,
      TransactionType transactionType,
      String documentNo
  ) {

    // 거래 유형 -> 판매(OUTBOUND)이면 SALE, 아니면 NONE
    SrcDocType srcDocType = transactionType == TransactionType.OUTBOUND
        ? SrcDocType.SALE
        : SrcDocType.NONE;

    // 참조 전표 번호
    String srcDocNo = documentNo;
    if (srcDocType.equals(SrcDocType.SALE)) {
      srcDocNo = documentNo + "SALE";
    }

    // 현재 총 재고 수량
    BigDecimal totalQty = stockService.getTotalStock(item.getItemId());

    // 출고 진행하면서 계속 줄여나갈 running total
    BigDecimal runningTotalQty = totalQty;

    // 출고할 재고들을 lot/location 단위로 소진
    List<Stock> stocks = stockService.removeStock(item.getItemId(), movedQty, user);
  for (Stock stock : stocks) {
      // 이번 stock에서 실제 빠진 양 (removeStock()에서 currentQty = removeQty 로 세팅됨)
      //BigDecimal movedQtyForStock = stock.getQtyRemoved();
      //runningTotalQty = runningTotalQty.subtract(movedQtyForStock);


      // 재고 수불부 기록
      StockMovement stockMovement = StockMovement.builder()
          .departWarehouse(stock.getLocation().getWarehouse())
          .arriveWarehouse(null)
          .user(user)
          .lotNumber(stock.getLotNumber())
          .transactionType(transactionType)     // OUTBOUND or DISCARD
         // .movedQty(movedQtyForStock.negate())  // 실제로 빠진 양 (음수 처리)
          .srcDocType(srcDocType)
          .srcDocNo(srcDocNo)
          .totalQty(runningTotalQty)            // 각 시점의 전체 잔량
          .build();

      stockMovementRepository.save(stockMovement);
    }

    // 안전재고 확인
    stockService.alertBelowSafetyStock(item.getItemId());
  }


  //직접 재고 수정 x, adjust 용도
  //실제 재고에 변경 없으므로 audit x
  public StockMovementResponse createAdjustStockMovement(UpdateStockMovementRequest updateStockMovementRequest){

    Item item = itemRepository.findByName(updateStockMovementRequest.getItemName())
        .orElseThrow(() -> new EntityNotFoundException("item not found by name"));

    User user = userRepository.findByName(updateStockMovementRequest.getUserName())
        .orElseThrow(() -> new EntityNotFoundException("user not found by name"));

    BigDecimal movedQty = updateStockMovementRequest.getMovedQty();

    BigDecimal totalQty = stockService.getTotalStock(item.getItemId());

    LotNumber lotNumber = lotNumberService.createLotNumberForStock(item, movedQty);

    StockMovement stockMovement = StockMovement.builder()
        .departWarehouse(null)
        .arriveWarehouse(null)
        .user(user)
        .lotNumber(lotNumber)
        .movedQty(movedQty) //음수인 경우 -> response용 구별을 위해 음수 그대로
        .srcDocType(null)
        .srcDocNo(null)
        .totalQty(totalQty.add(movedQty)) //양수 or 음수 둘다 반영됨
        .transactionType(TransactionType.ADJUSTMENT)
        .build();

    stockMovementRepository.save(stockMovement);

    return toStockMovementResponse(stockMovement);

  }

  //수정 - 이력이므로 수정 x

  public StockMovementResponse toStockMovementResponse(StockMovement stockMovement){

    String itemName = null;
    if(stockMovement.getLotNumber() != null && stockMovement.getLotNumber().getItem() != null){
      itemName = stockMovement.getLotNumber().getItem().getName();
    }

    String companyName = null; //stock movement의 user은 null 가능

    if (stockMovement.getUser() != null) {
      Client client = stockMovement.getUser().getClient(); //user 에 대해 client null 가능
      if (client != null) {
        companyName = client.getCompanyName();
      }
    }

    return StockMovementResponse.builder()
        .companyName(companyName)
        //stock movement - 구매, 판매, 생산에 대해 모두 lot no 가짐 -> adjustment에서는 x
        .itemName(itemName)
        .inboundQty(
            stockMovement.getMovedQty().compareTo(BigDecimal.ZERO) > 0
                ? stockMovement.getMovedQty()
                : BigDecimal.ZERO
        )
        .outboundQty(
            stockMovement.getMovedQty().compareTo(BigDecimal.ZERO) < 0
                ? stockMovement.getMovedQty()
                : BigDecimal.ZERO
        )
        .totalQty(stockMovement.getTotalQty())
        .createdAt(stockMovement.getCreatedAt())
        .build();
  }



}
