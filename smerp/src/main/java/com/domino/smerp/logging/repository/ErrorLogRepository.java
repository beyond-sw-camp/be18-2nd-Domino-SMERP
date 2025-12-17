package com.domino.smerp.logging.repository;

import com.domino.smerp.logging.domain.ErrorLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ErrorLogRepository
    extends ElasticsearchRepository<ErrorLog, String> {
}