package com.domino.smerp.logging.dto.condition;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminActionLogSearchCondition {

    private final LocalDateTime from;
    private final LocalDateTime to;

    private final String action;
    private final String entity;
    private final String entityId;
    private final Boolean success;
    private final String actor;
}
