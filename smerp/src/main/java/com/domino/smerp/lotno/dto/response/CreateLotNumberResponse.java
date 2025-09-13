package com.domino.smerp.lotno.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateLotNumberResponse {

  private Long lotId;
  private String itemId;
  private String status;


}
