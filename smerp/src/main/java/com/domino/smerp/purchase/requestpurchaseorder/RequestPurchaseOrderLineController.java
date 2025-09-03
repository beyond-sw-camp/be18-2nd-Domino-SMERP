package com.domino.smerp.purchase.requestpurchaseorder;

import com.domino.smerp.purchase.requestpurchaseorder.dto.ItemRpoCrossedTableRequest;
import com.domino.smerp.purchase.requestpurchaseorder.dto.ItemRpoCrossedTableResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rpos/{rpoId}/lines")
public class RequestPurchaseOrderLineController {

  private final ItemRpoCrossedTableService service;

  /**
   * 라인 추가(없으면 생성, 있으면 수량 치환 정책)
   */
  @PostMapping
  public ResponseEntity<ItemRpoCrossedTableResponse> addLine(
      @PathVariable Long rpoId,
      @Valid @RequestBody ItemRpoCrossedTableRequest req
  ) {
    return ResponseEntity.ok(ItemRpoCrossedTableResponse.from(
        service.addLine(rpoId, req.itemId(), req.qty())
    ));
  }

  /**
   * 라인 수량 변경(PATCH)
   */
  @PatchMapping("/{itemId}")
  public ResponseEntity<ItemRpoCrossedTableResponse> changeQty(
      @PathVariable Long rpoId,
      @PathVariable Long itemId,
      @RequestBody ItemRpoCrossedTableRequest req
  ) {
    return ResponseEntity.ok(ItemRpoCrossedTableResponse.from(
        service.changeQty(rpoId, itemId, req.qty())
    ));
  }

  /**
   * 라인 삭제
   */
  @DeleteMapping("/{itemId}")
  public ResponseEntity<Void> removeLine(
      @PathVariable Long rpoId,
      @PathVariable Long itemId
  ) {
    service.removeLine(rpoId, itemId);
    return ResponseEntity.noContent().build();
  }

  /**
   * 라인 목록
   */
  @GetMapping
  public ResponseEntity<Page<ItemRpoCrossedTableResponse>> list(
      @PathVariable Long rpoId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "50") int size
  ) {
    Page<ItemRpoCrossedTableResponse> body =
        service.listByRpo(rpoId, PageRequest.of(page, size))
            .map(ItemRpoCrossedTableResponse::from);
    return ResponseEntity.ok(body);
  }
}
