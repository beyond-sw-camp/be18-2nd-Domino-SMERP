package com.domino.smerp.bom.controller;

import com.domino.smerp.bom.dto.request.CreateBomRequest;
import com.domino.smerp.bom.dto.response.CreateBomResponse;
import com.domino.smerp.bom.service.BomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/boms")
@RequiredArgsConstructor
public class BomController {

  private final BomService bomService;

  @PostMapping
  public ResponseEntity<CreateBomResponse> createBom(
      final @Valid @RequestBody CreateBomRequest request) {
    return ResponseEntity.ok(bomService.createBom(request));
  }


}
