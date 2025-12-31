package com.domino.smerp.logging.repository;

import com.domino.smerp.logging.domain.ActionLog;
import com.domino.smerp.logging.dto.condition.AdminActionLogSearchCondition;
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
public class AdminActionLogSearchRepositoryImpl
    implements AdminActionLogSearchRepository {

    private final ElasticsearchOperations operations;

    @Override
    public Page<ActionLog> search(
        AdminActionLogSearchCondition cond,
        Pageable pageable
    ) {
        Criteria criteria = new Criteria();

        if (cond.getFrom() != null && cond.getTo() != null) {
            criteria = criteria.and(
                Criteria.where("timestamp")
                    .between(cond.getFrom(), cond.getTo())
            );
        }
        if (cond.getAction() != null) {
            criteria = criteria.and("action").is(cond.getAction());
        }
        if (cond.getEntity() != null) {
            criteria = criteria.and("entity").is(cond.getEntity());
        }
        if (cond.getEntityId() != null) {
            criteria = criteria.and("entityId").is(cond.getEntityId());
        }
        if (cond.getSuccess() != null) {
            criteria = criteria.and("success").is(cond.getSuccess());
        }
        if (cond.getActor() != null) {
            criteria = criteria.and("actor").is(cond.getActor());
        }

        Query query = new CriteriaQuery(criteria)
            .setPageable(pageable);

        SearchHits<ActionLog> hits =
            operations.search(query, ActionLog.class);

        List<ActionLog> content = hits.getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .toList();

        return new PageImpl<>(content, pageable, hits.getTotalHits());
    }
}
