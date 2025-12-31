package com.domino.smerp.logging.repository;

import com.domino.smerp.logging.domain.ActionLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ActionLogRepository
    extends ElasticsearchRepository<ActionLog, String> {
}