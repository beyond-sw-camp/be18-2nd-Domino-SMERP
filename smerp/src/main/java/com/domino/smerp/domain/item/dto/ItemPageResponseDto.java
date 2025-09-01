package com.domino.smerp.domain.item.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemPageResponseDto {
  private long totalElements;
  private int totalPages;
  private int currentPage;
  private int size;
  private boolean isFirst;
  private boolean isLast;
  private List<ItemResponseDto> content;

  @Builder
  public ItemPageResponseDto(long totalElements,
      int totalPages,
      int currentPage,
      int size,
      boolean isFirst,
      boolean isLast,
      List<ItemResponseDto> content) {
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.currentPage = currentPage;
    this.size = size;
    this.isFirst = isFirst;
    this.isLast = isLast;
    this.content = content;
  }
}
