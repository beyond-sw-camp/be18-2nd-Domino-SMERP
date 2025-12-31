package com.domino.smerp.logging.service;

import com.domino.smerp.logging.domain.ActionLog;
import com.domino.smerp.logging.dto.response.ActionLogDetailResponse;
import com.domino.smerp.logging.dto.response.ActionLogResponse;
import com.domino.smerp.logging.dto.condition.AdminActionLogSearchCondition;
import com.domino.smerp.logging.repository.ActionLogRepository;
import com.domino.smerp.logging.repository.AdminActionLogSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminActionLogService {

    private final AdminActionLogSearchRepository repository;
    private final ActionLogRepository actionLogRepository;

    public Page<ActionLogResponse> search(
        final AdminActionLogSearchCondition cond,
        Pageable pageable
    ) {
        return repository.search(cond, pageable)
            .map(ActionLogResponse::from);
    }

    public ActionLogDetailResponse getDetailLog(final String id) {
        ActionLog log = actionLogRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("행위 로그를 찾을 수 없습니다."));

        return ActionLogDetailResponse.from(log);
    }
}
