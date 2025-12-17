package com.domino.smerp.logging.repository;

import com.domino.smerp.logging.domain.ApiLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ApiLogRepository
    extends ElasticsearchRepository<ApiLog, String> {
}
