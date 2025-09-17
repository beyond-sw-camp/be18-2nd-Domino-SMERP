package com.domino.smerp.bom.dto.response;

import com.domino.smerp.bom.entity.Bom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateBomResponse {

  private final Long bomId;
  private final String message;

  public static CreateBomResponse fromEntity(final Bom bom) {
    return CreateBomResponse.builder()
        .bomId(bom.getBomId())
        .message("BOM이 성공적으로 생성되었습니다.")
        .build();
  }
}

