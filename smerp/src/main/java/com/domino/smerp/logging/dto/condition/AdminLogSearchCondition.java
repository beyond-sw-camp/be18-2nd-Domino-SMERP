package com.domino.smerp.logging.dto.condition;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminLogSearchCondition {

    private final LocalDateTime from;
    private final LocalDateTime to;

    private final String method;
    private final Integer status;
    private final String uri;
    private final String principal;
}
