package com.domino.smerp.location.service;

import com.domino.smerp.item.Item;
import com.domino.smerp.item.repository.ItemRepository;
import com.domino.smerp.location.Location;
import com.domino.smerp.location.LocationRepository;
import com.domino.smerp.location.dto.request.LocationIdListRequest;
import com.domino.smerp.location.dto.response.LocationIdListResponse;
import com.domino.smerp.location.dto.response.LocationListResponse;
import com.domino.smerp.location.dto.response.LocationResponse;
import com.domino.smerp.stockmovement.StockMovement;
import com.domino.smerp.stockmovement.StockMovementRepository;
import com.domino.smerp.stockmovement.constants.SrcDocType;
import com.domino.smerp.stockmovement.constants.TransactionType;
import com.domino.smerp.user.User;
import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.WarehouseRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.domino.smerp.stock.Stock;
import com.domino.smerp.stock.StockRepository;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

  private final WarehouseRepository warehouseRepository;
  private final LocationRepository locationRepository;
  private final StockRepository stockRepository;
  private final ItemRepository itemRepository;
  private final StockMovementRepository stockMovementRepository;

  //칸 생성(창고 생성 시)
  @Override
  @Transactional
  public void createLocation(final Long warehouseId) {

    Warehouse warehouse = warehouseRepository.findById(warehouseId)
        .orElseThrow(() -> new EntityNotFoundException("창고를 찾을 수 없습니다."));

    List<String> racks = parseFixedRange("R", 1, 20);
    List<String> levels = parseFixedRange("L", 1, 5);
    List<String> bins = parseFixedRange("B", 1, 30);

    //warehouse는 id 기준
    String w = parseId("W", warehouse.getId(), 2);

    List<Location> locations = new ArrayList<>();

    //rfid 생성, location 저장
    for (String r : racks) {
      for (String l : levels) {
        for (String b : bins) {
          String rfid = generateRfid(w, r, l, b);
          Location location = Location.create(r, l, b, rfid, warehouse);
          locations.add(location);
          warehouse.getLocations().add(location); //
        }
      }
    }

    List<Location> savedLocations = locationRepository.saveAll(locations);
    // 확인용 출력
    /*
    savedLocations.forEach(loc -> System.out.println(
        loc.getId() + " " + loc.getRackNo() + " " + loc.getLevelNo() + " " + loc.getBinNo() + " " + loc.getBinRfid()
    ));
     */


  }

  public List<String> parseFixedRange(final String prefix, final int start, final int end) {
    List<String> result = new ArrayList<>();
    for (int i = start; i <= end; i++) {
      if (i < 10) { //10 밑 R01..
        result.add(prefix + String.format("%02d", i));
      } else { //10부터는 R10..
        result.add(prefix + i);
      }
    }
    return result;
  }

  public String parseId(final String prefix, final Long id, final Integer padding) {

    String result = "";
    if (id < 10) { //10 밑 W01..
      result = prefix + String.format("%02d", id);
    } else { //10부터 W10..
      result = prefix + id;
    }
    return result;
  }

  public String generateRfid(final String w, final String r, final String l, final String b) {
    String rfid = "RFID" + "-" + w + "-" + r + "-" + l + "-" + b;
    return rfid;
  }

  //빈 칸 반환
  @Transactional(readOnly = true)
  @Override
  public LocationListResponse getUnFilledLocations(final Long warehouseId) {

    List<Location> unFilledLocations = locationRepository.findAllByWarehouseIdAndFilledFalse(
        warehouseId);

    List<LocationResponse> responses = new ArrayList<>();

    for (Location location : unFilledLocations) {

      responses.add(LocationResponse.builder()
          .id(location.getId())
          .rackNo(location.getRackNo())
          .levelNo(location.getLevelNo())
          .binNo(location.getBinNo())
          .filled(location.isFilled())
          .build());
    }

    return LocationListResponse.builder()
        .locationResponses(responses)
        .build();

  }

  //칸 채움
  @Transactional
  @Override
  public LocationIdListResponse fillLocations(final LocationIdListRequest locationIdListRequest) {

    LocationIdListResponse locationIdListResponse = LocationIdListResponse.builder().build();

    for (Long id : locationIdListRequest.getLocationIds()) {

      //유효 x id
      Location location = locationRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException("id에 해당하는 location 없음"));

      location.setFilled(true);
      locationIdListResponse.getLocationIds().add(location.getId());

    }

    return locationIdListResponse;

  }

  //칸 비움
  @Transactional
  @Override
  public LocationIdListResponse unFillLocations(final LocationIdListRequest locationIdListRequest) {

    LocationIdListResponse locationIdListResponse = LocationIdListResponse.builder().build();

    for (Long id : locationIdListRequest.getLocationIds()) {
      Location location = locationRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException("id에 해당하는 location 없음"));

      location.setFilled(false);
      locationIdListResponse.getLocationIds().add(location.getId());
    }

    return locationIdListResponse;

  }

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

    List<Stock> createdStocks = new ArrayList<>();

    for (Warehouse warehouse : availableWarehouses) {

      List<Location> locations = locationRepository.findAvailableLocationsWithCurQty(
          warehouse.getId(),
          remainingQty
      );

      for (Location loc : locations) {
        //각 위치의 남은 공간
        BigDecimal available = loc.getMaxQty().subtract(
            loc.getCurQty() != null ? loc.getCurQty() : BigDecimal.ZERO
        ); //cur qty 있으면 해당 값으로 빼기, 없으면 0 

        if(available.compareTo(remainingQty) <= 0) continue;

        //남은 공간보다 가용 공간 크면 남은 공간만큼
        //남은 공간보다 가용 공간이 작으면 남은 공간만큼
        BigDecimal allocateQty = remainingQty.min(available);

        Stock stock = Stock.builder()
            .location(loc)
            .lotNo(null)
            .item(item)
            .qty(allocateQty)
            .rfid(null)
            .build();

        stockRepository.save(stock);

        createdStocks.add(stock);

        loc.setCurQty(
            (loc.getCurQty() != null ? loc.getCurQty() : BigDecimal.ZERO)
                .add(allocateQty)
        );

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
  public List<Stock> removeStockForSale(Long itemId, BigDecimal qty, User user) {
    
    BigDecimal remainQty = qty; //빼야할 수량

    List<Warehouse> availableWarehouses = warehouseRepository.findWarehousesWithStock(itemId);
    List<Stock> stocksRemoved = new ArrayList<>();

    for(Warehouse warehouse : availableWarehouses) {

      //출발 창고
      if(remainQty.compareTo(BigDecimal.ZERO) <= 0) break;

      //위치 단위로 재고 소진
      List<Stock> stocks = stockRepository.findByItemIdAndWarehouseId(itemId, warehouse.getId());



      for(Stock stock : stocks) {
        if(remainQty.compareTo(BigDecimal.ZERO) <= 0) break;

        //위치에 남아있는 재고 > 출고수량 : 출고수량만큼만 빼기
        //위치에 남은 재고 < 출고수량 : 남아있는 재고만큼만 빼기

        Location location = stock.getLocation();

        //위치 중 해당 재고 자체만의 수량으로 비교
        BigDecimal removeQty = stock.getQty().min(remainQty);

        //현 위치의 재고 감소
        stock.setQty(stock.getQty().subtract(removeQty));
        stockRepository.save(stock);

        location.setCurQty(location.getCurQty().subtract(removeQty));
        locationRepository.save(location);

        //출고해야할 수량 감소
        remainQty = remainQty.subtract(removeQty);

        //재고 qty == 0이더라도 유지 -> 재고 삭제 x

        stocksRemoved.add(stock);

      }
    }
    return stocksRemoved;
  }



  public LocationResponse toLocationResponse(final Location location) {
    return LocationResponse.builder()
        .id(location.getId())
        .rackNo(location.getRackNo())
        .levelNo(location.getLevelNo())
        .binNo(location.getBinNo())
        .filled(location.isFilled())
        .build();

  }
}
