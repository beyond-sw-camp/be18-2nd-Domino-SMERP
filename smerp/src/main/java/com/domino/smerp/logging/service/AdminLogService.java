package com.domino.smerp.logging.service;

import com.domino.smerp.logging.dto.condition.AdminLogSearchCondition;
import com.domino.smerp.logging.dto.response.ApiLogResponse;
import com.domino.smerp.logging.repository.AdminApiLogSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLogService {

    private final AdminApiLogSearchRepository apiLogSearchRepository;

    public Page<ApiLogResponse> searchApiLogs(
        final AdminLogSearchCondition cond,
        Pageable pageable
    ) {
        return apiLogSearchRepository.search(cond, pageable)
            .map(ApiLogResponse::from);
    }
}
