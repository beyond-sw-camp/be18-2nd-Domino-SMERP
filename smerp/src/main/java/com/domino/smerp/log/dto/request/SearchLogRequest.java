package com.domino.smerp.log.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SearchLogRequest {
    private final String action;
    private final String actor;
    private final String entity;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
}
