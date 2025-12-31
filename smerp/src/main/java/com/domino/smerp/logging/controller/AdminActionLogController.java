package com.domino.smerp.logging.controller;

import com.domino.smerp.logging.dto.response.ActionLogDetailResponse;
import com.domino.smerp.logging.dto.response.ActionLogResponse;
import com.domino.smerp.logging.dto.condition.AdminActionLogSearchCondition;
import com.domino.smerp.logging.service.AdminActionLogService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/logs/actions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminActionLogController {

    private final AdminActionLogService service;

    @GetMapping
    public Page<ActionLogResponse> search(
        @RequestParam(required = false) final LocalDateTime from,
        @RequestParam(required = false) final LocalDateTime to,
        @RequestParam(required = false) final String action,
        @RequestParam(required = false) final String entity,
        @RequestParam(required = false) final String entityId,
        @RequestParam(required = false) final Boolean success,
        @RequestParam(required = false) final String actor,
        @PageableDefault(size = 20, sort = "timestamp", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        AdminActionLogSearchCondition cond =
            AdminActionLogSearchCondition.builder()
                .from(from)
                .to(to)
                .action(action)
                .entity(entity)
                .entityId(entityId)
                .success(success)
                .actor(actor)
                .build();

        return service.search(cond, pageable);
    }

    @GetMapping("/{id}")
    public ActionLogDetailResponse detail(
        @PathVariable final String id
    ) {
        return service.getDetailLog(id);
    }
}
