package com.domino.smerp.log.repository;

import com.domino.smerp.log.dto.request.SearchLogRequest;
import com.domino.smerp.log.dto.response.LogListResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LogQueryRepositoryImpl implements LogQueryRepository {
    private final JPAQueryFactory queryFactory;


    @Override
    public Page<LogListResponse> searchLogs(final SearchLogRequest keyword, final Pageable pageable) {
        return null;
    }
}