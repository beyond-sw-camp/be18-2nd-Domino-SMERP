package com.domino.smerp.logging.dto.condition;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminErrorLogSearchCondition {

    private final LocalDateTime from;
    private final LocalDateTime to;

    private final Integer status;
    private final String uri;
    private final String principal;
    private final String exceptionClass;
}
