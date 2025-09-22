package com.domino.smerp.log.repository;

import com.domino.smerp.log.dto.request.SearchLogRequest;
import com.domino.smerp.log.dto.response.LogListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LogQueryRepository {
    Page<LogListResponse> searchLogs(SearchLogRequest keyword, Pageable pageable);
}
