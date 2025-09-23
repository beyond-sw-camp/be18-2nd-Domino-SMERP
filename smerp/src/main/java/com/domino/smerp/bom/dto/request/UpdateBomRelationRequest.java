package com.domino.smerp.bom.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBomRelationRequest {

  @NotNull(message = "부모 품목 ID는 필수입니다.")
  private Long newParentItemId;

}