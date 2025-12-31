package com.domino.smerp.logging.service;

import com.domino.smerp.logging.domain.ActionLog;
import com.domino.smerp.logging.repository.ActionLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActionLogService {

    private final ActionLogRepository repository;

    public void success(
        String action,
        String entity,
        String entityId,
        String after,
        HttpServletRequest request
    ) {
        repository.save(ActionLog.builder()
            .action(action)
            .entity(entity)
            .entityId(entityId)
            .success(true)
            .afterData(after)
            .actor(getPrincipal())
            .clientIp(request.getRemoteAddr())
            .timestamp(LocalDateTime.now())
            .build()
        );
    }

    public void fail(
        String action,
        String entity,
        String entityId,
        String reason,
        String before,
        HttpServletRequest request
    ) {
        repository.save(ActionLog.builder()
            .action(action)
            .entity(entity)
            .entityId(entityId)
            .success(false)
            .failReason(reason)
            .beforeData(before)
            .actor(getPrincipal())
            .clientIp(request.getRemoteAddr())
            .timestamp(LocalDateTime.now())
            .build()
        );
    }

    private String getPrincipal() {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "SYSTEM";
    }
}
