package com.domino.smerp.log.dto;

import com.domino.smerp.log.ActionType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LogResponse {
    private final LocalDateTime doAt;
    private final String entity;
    private final ActionType action;
    private final String actor;
    private final String beforeData;
    private final String afterData;
}
