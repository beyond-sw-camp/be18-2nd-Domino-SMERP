package com.domino.smerp.logging.repository;

import com.domino.smerp.logging.domain.ErrorLog;
import com.domino.smerp.logging.dto.condition.AdminErrorLogSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminErrorLogSearchRepository {
    Page<ErrorLog> search(AdminErrorLogSearchCondition condition, Pageable pageable);
}
