package com.domino.smerp.purchase.requestorder;

import com.domino.smerp.purchase.itemrocrossedtable.ItemRoCrossedTableService;
import com.domino.smerp.purchase.requestorder.dto.request.RequestOrderRequest;
import com.domino.smerp.purchase.requestorder.dto.response.RequestOrderResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 발주 품목 라인 Controller
 */
@RestController
@RequestMapping("/api/request-orders/{roId}/lines")
@RequiredArgsConstructor
public class RequestOrderLineController {

  private final ItemRoCrossedTableService itemRoCrossedTableService;

  /**
   * 발주 품목 라인 추가
   */
  @PostMapping
  public ResponseEntity<RequestOrderResponse.RequestOrderLineResponse> addLine(
      @PathVariable final Long roId,
      @RequestBody final RequestOrderRequest.RequestOrderLineRequest request) {
    return ResponseEntity.status(201)
        .body(itemRoCrossedTableService.addLine(roId, request));
  }

  /**
   * 발주 품목 라인 전체 조회
   */
  @GetMapping
  public ResponseEntity<List<RequestOrderResponse.RequestOrderLineResponse>> getLines(
      @PathVariable final Long roId) {
    return ResponseEntity.ok(itemRoCrossedTableService.getLinesByRoId(roId));
  }

  /**
   * 발주 품목 라인 수정 (예: 수량 변경)
   */
  @PatchMapping("/{lineId}")
  public ResponseEntity<RequestOrderResponse.RequestOrderLineResponse> updateLine(
      @PathVariable final Long roId,
      @PathVariable final Long lineId,
      @RequestBody final RequestOrderRequest.RequestOrderLineRequest request) {
    return ResponseEntity.ok(itemRoCrossedTableService.updateLine(roId, lineId, request));
  }

  /**
   * 발주 품목 라인 삭제
   */
  @DeleteMapping("/{lineId}")
  public ResponseEntity<Void> deleteLine(
      @PathVariable final Long roId,
      @PathVariable final Long lineId) {
    itemRoCrossedTableService.deleteLine(roId, lineId);
    return ResponseEntity.noContent().build(); // 204
  }
}
