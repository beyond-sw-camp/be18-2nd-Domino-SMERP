package com.domino.smerp.logging.controller;

import com.domino.smerp.logging.dto.condition.AdminErrorLogSearchCondition;
import com.domino.smerp.logging.dto.response.ErrorLogResponse;
import com.domino.smerp.logging.service.AdminErrorLogService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logs/errors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminErrorLogController {

    private final AdminErrorLogService service;

    @GetMapping
    public Page<ErrorLogResponse> search(
        @RequestParam(required = false) final LocalDateTime from,
        @RequestParam(required = false) final LocalDateTime to,
        @RequestParam(required = false) final Integer status,
        @RequestParam(required = false) final String uri,
        @RequestParam(required = false) final String principal,
        @RequestParam(required = false) final String exceptionClass,
        @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        AdminErrorLogSearchCondition cond =
            AdminErrorLogSearchCondition.builder()
                .from(from)
                .to(to)
                .status(status)
                .uri(uri)
                .principal(principal)
                .exceptionClass(exceptionClass)
                .build();

        return service.searchErrorLogs(cond, pageable);
    }
}
