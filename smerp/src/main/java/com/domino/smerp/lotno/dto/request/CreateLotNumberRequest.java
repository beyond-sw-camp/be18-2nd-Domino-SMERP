package com.domino.smerp.lotno.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateLotNumberRequest {

  private final Long itemId;
  private final String name;
  private final String status;



}
