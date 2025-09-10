package com.domino.smerp.common.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

@Getter
@AllArgsConstructor
public class PageResponse<T> {
  private int page;               // 현재 페이지 (0부터 시작)
  private int size;               // 요청한 페이지 크기
  private long totalElements;     // 전체 데이터 개수
  private int totalPages;         // 전체 페이지 수
  private boolean first;           // 첫 페이지 여부
  private boolean last;           // 마지막 페이지 여부
  private List<SortInfo> sort;    // 정렬 기준 / 정렬 순서
  private List<T> content;        // 실제 데이터 목록

  // 정적 팩토리 메서드
  public static <T> PageResponse<T> from(Page<T> page) {
    return new PageResponse<>(
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.isFirst(),
        page.isLast(),
        page.getSort().stream()
            .map(SortInfo::from)
            .collect(Collectors.toList()),
        page.getContent()
    );
  }

  // 정렬 정보 DTO
  @Getter
  @AllArgsConstructor
  public static class SortInfo {
    private String property;   // 정렬 기준 컬럼
    private String direction;  // default: ASC

    public static SortInfo from(Sort.Order order) {
      return new SortInfo(order.getProperty(), order.getDirection().name());
    }
  }
}