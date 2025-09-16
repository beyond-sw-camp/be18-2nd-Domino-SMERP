package com.domino.smerp.salesorder.repository;

import com.domino.smerp.client.QClient;
import com.domino.smerp.order.QOrder;
import com.domino.smerp.salesorder.QSalesOrder;
import com.domino.smerp.salesorder.SalesOrder;
import com.domino.smerp.salesorder.constants.SalesOrderStatus;
import com.domino.smerp.salesorder.dto.request.SearchSalesOrderRequest;
import com.domino.smerp.user.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SalesOrderQueryRepositoryImpl implements SalesOrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SalesOrder> searchSalesOrders(SearchSalesOrderRequest condition, Pageable pageable) {
        QSalesOrder so = QSalesOrder.salesOrder;
        QOrder order = QOrder.order;
        QClient client = QClient.client;
        QUser user = QUser.user;

        List<SalesOrder> results = queryFactory
                .selectFrom(so)
                .join(so.order, order).fetchJoin()
                .join(order.client, client).fetchJoin()
                .join(order.user, user).fetchJoin()
                .where(
                        companyNameContains(condition.getCompanyName()),
                        statusEq(condition.getStatus()),
                        userNameContains(condition.getUserName()),
                        documentNoContains(condition.getDocumentNo()),
                        remarkContains(condition.getRemark()),
                        documentNoBetween(condition.getStartDocDate(), condition.getEndDocDate())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(so.count())
                .from(so)
                .join(so.order, order)
                .join(order.client, client)
                .join(order.user, user)
                .where(
                        companyNameContains(condition.getCompanyName()),
                        statusEq(condition.getStatus()),
                        userNameContains(condition.getUserName()),
                        documentNoContains(condition.getDocumentNo()),
                        remarkContains(condition.getRemark()),
                        documentNoBetween(condition.getStartDocDate(), condition.getEndDocDate())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanExpression companyNameContains(String name) {
        return (name == null || name.isEmpty()) ? null : QClient.client.companyName.contains(name);
    }

    private BooleanExpression statusEq(SalesOrderStatus status) {
        return (status == null) ? null : QSalesOrder.salesOrder.status.eq(status);
    }

    private BooleanExpression userNameContains(String userName) {
        return (userName == null || userName.isEmpty()) ? null : QUser.user.name.contains(userName);
    }

    private BooleanExpression documentNoContains(String documentNo) {
        return (documentNo == null || documentNo.isEmpty()) ? null : QSalesOrder.salesOrder.documentNo.contains(documentNo);
    }

    private BooleanExpression remarkContains(String remark) {
        return (remark == null || remark.isEmpty()) ? null : QSalesOrder.salesOrder.remark.contains(remark);
    }

    private BooleanExpression documentNoBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return null;

        String startStr = start.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String endStr = end.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        return QSalesOrder.salesOrder.documentNo.substring(0, 10).between(startStr, endStr);
    }
}
