package com.domino.smerp.logging.controller;

import com.domino.smerp.logging.dto.condition.AdminLogSearchCondition;
import com.domino.smerp.logging.dto.response.ApiLogResponse;
import com.domino.smerp.logging.service.AdminLogService;
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
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminApiLogController {

    private final AdminLogService logService;

    @GetMapping("/apis")
    public Page<ApiLogResponse> searchApiLogs(
        @RequestParam(required = false) LocalDateTime from,
        @RequestParam(required = false) LocalDateTime to,
        @RequestParam(required = false) String method,
        @RequestParam(required = false) Integer status,
        @RequestParam(required = false) String uri,
        @RequestParam(required = false) String principal,
        @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        AdminLogSearchCondition cond =
            AdminLogSearchCondition.builder()
                .from(from)
                .to(to)
                .method(method)
                .status(status)
                .uri(uri)
                .principal(principal)
                .build();

        return logService.searchApiLogs(cond, pageable);
    }
}
