package com.domino.smerp.logging.repository;

import com.domino.smerp.logging.domain.ActionLog;
import com.domino.smerp.logging.dto.condition.AdminActionLogSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminActionLogSearchRepository {
    Page<ActionLog> search(AdminActionLogSearchCondition cond, Pageable pageable);
}
