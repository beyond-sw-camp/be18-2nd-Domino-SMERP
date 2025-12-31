package com.domino.smerp.logging.repository;

import com.domino.smerp.logging.domain.ErrorLog;
import com.domino.smerp.logging.dto.condition.AdminErrorLogSearchCondition;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminErrorLogSearchRepositoryImpl
    implements AdminErrorLogSearchRepository {

    private final ElasticsearchOperations operations;

    @Override
    public Page<ErrorLog> search(
        AdminErrorLogSearchCondition cond,
        Pageable pageable
    ) {
        Criteria criteria = new Criteria();

        if (cond.getFrom() != null && cond.getTo() != null) {
            criteria = criteria.and(
                Criteria.where("timestamp")
                    .between(cond.getFrom(), cond.getTo())
            );
        }

        if (cond.getStatus() != null) {
            criteria = criteria.and("status").is(cond.getStatus());
        }

        if (cond.getUri() != null) {
            criteria = criteria.and("uri").contains(cond.getUri());
        }

        if (cond.getPrincipal() != null) {
            criteria = criteria.and("principal").is(cond.getPrincipal());
        }

        if (cond.getExceptionClass() != null) {
            criteria = criteria.and("exceptionClass")
                .contains(cond.getExceptionClass());
        }

        Query query = new CriteriaQuery(criteria)
            .setPageable(pageable);

        SearchHits<ErrorLog> hits =
            operations.search(query, ErrorLog.class);

        List<ErrorLog> content = hits.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .toList();

        return new PageImpl<>(content, pageable, hits.getTotalHits());
    }
}