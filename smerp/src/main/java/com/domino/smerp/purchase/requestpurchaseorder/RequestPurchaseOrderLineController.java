package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.itemrpocrossedtablerequest.ItemRpoCrossedTableService;
import com.domino.smerp.purchase.itemrpocrossedtablerequest.dto.request.ItemRpoCrossedTableRequest;
import com.domino.smerp.purchase.itemrpocrossedtablerequest.dto.response.ItemRpoCrossedTableResponse;
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
 * 구매요청 품목 라인(ItemRpoCrossedTable) Controller
 */
@RestController
@RequestMapping("/api/request-purchase-orders/{rpoId}/lines")
@RequiredArgsConstructor
public class RequestPurchaseOrderLineController {

  private final ItemRpoCrossedTableService itemRpoCrossedTableService;

  /**
   * 품목 라인 추가
   */
  @PostMapping
  public ResponseEntity<ItemRpoCrossedTableResponse> addLine(
      @PathVariable final Long rpoId,
      @RequestBody final ItemRpoCrossedTableRequest request) {
    ItemRpoCrossedTableResponse response = itemRpoCrossedTableService.addLine(rpoId, request);
    return ResponseEntity.status(201).body(response);
  }

  /**
   * 특정 RPO의 품목 라인 목록 조회
   */
  @GetMapping
  public ResponseEntity<List<ItemRpoCrossedTableResponse>> getLines(
      @PathVariable final Long rpoId) {
    List<ItemRpoCrossedTableResponse> responses = itemRpoCrossedTableService.getLinesByRpoId(rpoId);
    return ResponseEntity.ok(responses);
  }

  /**
   * 품목 라인 수정 (수량 변경 등)
   */
  @PatchMapping("/{lineId}")
  public ResponseEntity<ItemRpoCrossedTableResponse> updateLine(
      @PathVariable final Long rpoId,
      @PathVariable final Long lineId,
      @RequestBody final ItemRpoCrossedTableRequest request) {
    ItemRpoCrossedTableResponse response = itemRpoCrossedTableService.updateLine(rpoId, lineId,
        request);
    return ResponseEntity.ok(response);
  }

  /**
   * 품목 라인 삭제
   */
  @DeleteMapping("/{lineId}")
  public ResponseEntity<Void> deleteLine(
      @PathVariable final Long rpoId,
      @PathVariable final Long lineId) {
    itemRpoCrossedTableService.deleteLine(rpoId, lineId);
    return ResponseEntity.noContent().build(); // 204
  }
}
