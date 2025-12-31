package com.domino.smerp.logging.repository;

import com.domino.smerp.logging.domain.ApiLog;
import com.domino.smerp.logging.dto.condition.AdminLogSearchCondition;
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
public class AdminApiLogSearchRepositoryImpl
    implements AdminApiLogSearchRepository {

    private final ElasticsearchOperations operations;

    @Override
    public Page<ApiLog> search(
        AdminLogSearchCondition cond,
        Pageable pageable
    ) {
        Criteria criteria = new Criteria();

        if (cond.getFrom() != null && cond.getTo() != null) {
            criteria = criteria.and(
                Criteria.where("timestamp")
                    .between(cond.getFrom(), cond.getTo())
            );
        }

        if (cond.getMethod() != null) {
            criteria = criteria.and("method").is(cond.getMethod());
        }

        if (cond.getStatus() != null) {
            criteria = criteria.and("status").is(cond.getStatus());
        } else {
            criteria = criteria.and("status").lessThan(400);
        }

        if (cond.getUri() != null) {
            criteria = criteria.and("uri").contains(cond.getUri());
        }

        if (cond.getPrincipal() != null) {
            criteria = criteria.and("principal").is(cond.getPrincipal());
        }

        Query query = new CriteriaQuery(criteria).setPageable(pageable);

        SearchHits<ApiLog> hits =
            operations.search(query, ApiLog.class);

        List<ApiLog> content = hits.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .toList();

        return new PageImpl<>(content, pageable, hits.getTotalHits());
    }
}

