package com.domino.smerp.logging.service;

import com.domino.smerp.logging.dto.condition.AdminErrorLogSearchCondition;
import com.domino.smerp.logging.dto.response.ErrorLogResponse;
import com.domino.smerp.logging.repository.AdminErrorLogSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminErrorLogService {

    private final AdminErrorLogSearchRepository searchRepository;

    public Page<ErrorLogResponse> searchErrorLogs(
        AdminErrorLogSearchCondition cond,
        Pageable pageable
    ) {
        return searchRepository.search(cond, pageable)
            .map(ErrorLogResponse::from);
    }
}
