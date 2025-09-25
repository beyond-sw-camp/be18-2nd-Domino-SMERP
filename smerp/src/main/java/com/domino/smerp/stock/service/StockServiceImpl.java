package com.domino.smerp.stock.service;

import com.domino.smerp.item.Item;
import com.domino.smerp.item.repository.ItemRepository;
import com.domino.smerp.itemorder.ItemOrder;
import com.domino.smerp.itemorder.ItemOrderRepository;
import com.domino.smerp.location.Location;
import com.domino.smerp.location.LocationRepository;
import com.domino.smerp.lotno.LotNumber;
import com.domino.smerp.lotno.LotNumberService;
import com.domino.smerp.lotno.repository.LotNumberRepository;
import com.domino.smerp.user.User;
import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.repository.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.domino.smerp.stock.Stock;
import com.domino.smerp.stock.StockRepository;
import com.domino.smerp.stock.dto.response.StockListResponse;
import com.domino.smerp.stock.dto.response.StockResponse;
import com.domino.smerp.stock.event.StockAboveSafetyEvent;
import com.domino.smerp.stock.event.StockBelowSafetyEvent;

@RequiredArgsConstructor
@Service
public class StockServiceImpl implements StockService {
  private final StockRepository stockRepository;
  private final ItemRepository itemRepository;
  private final LocationRepository locationRepository;
  private final WarehouseRepository warehouseRepository;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final LotNumberService lotNumberService;
  private final ItemOrderRepository itemOrderRepository;

  //요청상 조회, 수정

  /*
  @Override
  @Transactional
  public void initializeStocksWithItems(){
    List<Item> items = itemRepository.findAll();

    for(Item item : items){
      if(!stockRepository.existsByItemId(item.getItemId())){
        Stock stock = Stock.create(item);
        stockRepository.save(stock);
      }
    }
  }
  */

  //목록 조회
  @Transactional(readOnly = true)
  @Override
  public StockListResponse getAllStocks(){
    //soft delete 아니므로
    List<Stock> stocks = stockRepository.findAll();

    List<StockResponse> stockResponses = new ArrayList<>();

    for(Stock stock : stocks) {
      stockResponses.add(toStockResponse(stock));
    }

    return StockListResponse.builder()
        .stockResponses(stockResponses)
        .build();
  }

  //상세 조회
  @Override
  @Transactional(readOnly = true)
  public StockResponse getStockById(Long stockId){
    Stock stock = stockRepository.findById(stockId)
        .orElseThrow(() -> new EntityNotFoundException("Stock not found by id"));
    return toStockResponse(stock);
  }

  //수정 - qty에 대해 직접 x => 재고수불로만 반영

  @Override
  @Transactional
  public List<Stock> allocateStock(Long itemId, BigDecimal qty) {

    List<Warehouse> availableWarehouses = warehouseRepository.findAvailableWarehousesWithCurQty();
    if (availableWarehouses.isEmpty()) {
      throw new RuntimeException("빈 위치가 있는 창고가 없습니다.");
    }


    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new EntityNotFoundException("item not found by id"));

    BigDecimal remainingQty = qty;

    BigDecimal runningTotal = getTotalStock(itemId);

    List<Stock> createdStocks = new ArrayList<>();

    LotNumber lotNumber = lotNumberService.createLotNumberForStock(item, qty);

    for (Warehouse warehouse : availableWarehouses) {

      List<Location> locations = locationRepository.findAvailableLocationsWithCurQty(
          warehouse.getId(),
          remainingQty
      );

      for (Location loc : locations) {

        //각 위치의 남은 공간
        //max, cur 기본값 있으므로 null check x
        BigDecimal available = loc.getMaxQty().subtract(loc.getCurQty());



        //남은 공간보다 가용 공간 크면 남은 공간만큼
        //남은 공간보다 가용 공간이 작으면 남은 공간만큼
        BigDecimal allocateQty = remainingQty.min(available);

        runningTotal = runningTotal.add(allocateQty);



        if(available.compareTo(BigDecimal.ZERO) <= 0) continue;

        Stock stock = Stock.builder()
            .location(loc)
            .lotNumber(lotNumber)
            .item(item)
            .currentQty(runningTotal) //매 회차마다 추가되는 총 재고수량 500 1000 1500
            .qty(allocateQty) //현 회차에 추가하는 수량 (위치 400 비었으면 500 중 400 가능) == 실제 재고 하나의 total
            .rfid(null)
            .build();


        stockRepository.save(stock);
        createdStocks.add(stock);

        //차지한 공간 -> 마찬가지로 null x
        loc.setCurQty(loc.getCurQty().add(allocateQty));
        locationRepository.save(loc);


        remainingQty = remainingQty.subtract(allocateQty);
        if (remainingQty.compareTo(BigDecimal.ZERO) == 0) break;
      }

      if (remainingQty.compareTo(BigDecimal.ZERO) == 0) break;
    }

    if (remainingQty.compareTo(BigDecimal.ZERO) > 0) {
      throw new RuntimeException("수량을 모두 넣을 공간이 없습니다. 남은 수량: " + remainingQty);
    }

    return createdStocks;
  }

  @Transactional
  @Override
  public List<Stock> removeStock(Long itemId, BigDecimal qty, User user) {

    BigDecimal remainQty = qty; //빼야할 수량

    List<Warehouse> availableWarehouses = warehouseRepository.findWarehousesWithStock(itemId);
    Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new EntityNotFoundException("item not found by id"));

    List<Stock> stocksRemoved = new ArrayList<>();
    LotNumber lotNumber = lotNumberService.createLotNumberForStock(item, qty);


    BigDecimal runningTotalQty = getTotalStock(itemId);

    for(Warehouse warehouse : availableWarehouses) {
      //빼야할 수량 없으면 x
      if(remainQty.compareTo(BigDecimal.ZERO) <= 0) break;

      //위치 단위로 재고 소진
      List<Stock> stocks = stockRepository.findByItemIdAndWarehouseId(itemId, warehouse.getId());

      for(Stock stock : stocks) {
        //빼야할 수량 없으면 x
        if(remainQty.compareTo(BigDecimal.ZERO) <= 0) break;

        //위치 중 해당 재고 자체만의 수량으로 비교

        //매 회차 빼줄 대상
        //위치에 남아있는 재고 > 출고수량 : 출고수량만큼만 빼기
        //위치에 남은 재고 < 출고수량 : 남아있는 재고만큼만 빼기
        BigDecimal removeQty = stock.getQty().min(remainQty);

        //현 위치의 재고 감소
        stock.setQty(stock.getQty().subtract(removeQty));
        stock.setLotNumber(lotNumber);
        //총 재고 수량 - 각 변화되는 값 누적
        runningTotalQty = runningTotalQty.subtract(removeQty);
        stock.setCurrentQty(stock.getQty().subtract(removeQty));

        stock.setCurrentQty(runningTotalQty);


        stockRepository.save(stock);

        Location location = stock.getLocation();
        location.setCurQty(location.getCurQty().subtract(removeQty));
        locationRepository.save(location);

        //출고해야할 수량 감소
        remainQty = remainQty.subtract(removeQty);

        //재고 qty == 0이더라도 유지 -> 재고 삭제 x
        stocksRemoved.add(stock);
        System.out.println("Stock " + stock.getId() + " currentQty=" + stock.getCurrentQty());
      }
    }
    return stocksRemoved;
  }


  //재고 삭제는 qty 0이어도 x
  //item과 매핑된다면 cascade

  //창고의 현 재고 > 안전재고 수량
  //품목 하나에 여러 재고 가능
  //총 재고양 >= 안전재고 수량이어야 함
  @Override
  @Transactional(readOnly = true)
  public void alertBelowSafetyStock(Long itemId) {
    if(!isAboveSafetyStock(itemId)) {
      applicationEventPublisher.publishEvent(

          //부족한 수량에 대해서는 itemId로 파악
          new StockBelowSafetyEvent(itemId)
      );
    }
  }

  @Override
  @Transactional
  public void alertAboveSafetyStock(Long itemId){
    if(isAboveSafetyStock(itemId)){
      applicationEventPublisher.publishEvent(
          new StockAboveSafetyEvent(itemId)
      );
    }
  }

  @Override
  @Transactional(readOnly = true)
  //총 재고 > 안전재고
  public boolean isAboveSafetyStock(Long itemId){

    Item item =  itemRepository.findById(itemId)
        .orElseThrow(() -> new EntityNotFoundException("no item of id"));

    //품목에 대한 총 재고
    BigDecimal totalQty = getTotalStock(itemId);

    //총 재고 > 안전재고
    return totalQty.compareTo(item.getSafetyStock()) > 0;

  }

  @Override
  @Transactional(readOnly = true)
  public boolean isAboveItemOrderQty(Long itemOrderId) {
    ItemOrder itemOrder = itemOrderRepository.findById(itemOrderId)
        .orElseThrow(() -> new EntityNotFoundException("no item order of id"));

    //해당 item의 총 재고
    BigDecimal totalQty = stockRepository.sumQuantityByItemId(itemOrder.getItem().getItemId());

    //총 재고 > 주문량
    return totalQty.compareTo(itemOrder.getQty()) > 0;
  }



  @Override
  @Transactional(readOnly = true)
  public BigDecimal getTotalStock(Long itemId) {
    BigDecimal totalQty = stockRepository.sumQuantityByItemId(itemId);
    return totalQty != null ? totalQty : BigDecimal.ZERO;
  }

  public StockResponse toStockResponse(Stock stock){

    Item item = stock.getLotNumber().getItem();

    return StockResponse.builder()
        .itemId(item.getItemId())
        .itemName(item.getName())
        .specification(item.getSpecification())
        .currentQty(stock.getQty())
        .build();
  }

}
