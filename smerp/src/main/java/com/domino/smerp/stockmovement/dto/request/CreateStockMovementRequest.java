package com.domino.smerp.stockmovement.dto.request;

import com.domino.smerp.stockmovement.constants.TransactionType;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
//구매, 판매, 반품(돌아옴), 폐기 시 이용
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateStockMovementRequest {

  //거래처
  @NotNull
  private final Long userId;

  @NotNull
  private final Long itemId;

  @NotNull
  private final String documentNo;

  @Nullable
  @Builder.Default
  private final BigDecimal movedQty =  BigDecimal.ZERO;

  private final TransactionType transactionType;
}
