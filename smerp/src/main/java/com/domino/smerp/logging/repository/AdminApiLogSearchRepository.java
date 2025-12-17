package com.domino.smerp.logging.repository;

import com.domino.smerp.logging.domain.ApiLog;
import com.domino.smerp.logging.dto.condition.AdminLogSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminApiLogSearchRepository {
    Page<ApiLog> search(AdminLogSearchCondition condition, Pageable pageable);
}
