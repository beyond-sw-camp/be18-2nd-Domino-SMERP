package com.domino.smerp.order.repository;

import com.domino.smerp.client.QClient;
import com.domino.smerp.order.Order;
import com.domino.smerp.order.QOrder;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.order.dto.request.OrderSearchRequest;
import com.domino.smerp.user.QUser;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> searchOrders(OrderSearchRequest condition, Pageable pageable) {
        QOrder order = QOrder.order;
        QClient client = QClient.client;
        QUser user = QUser.user;

        List<Order> results = queryFactory
                .selectFrom(order)
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
                .orderBy(getOrderSpecifiers(pageable, order))   // 정렬 적용
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
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

    // BooleanExpression
    private BooleanExpression companyNameContains(String name) {
        return (name == null || name.isEmpty()) ? null : QClient.client.companyName.contains(name);
    }

    private BooleanExpression statusEq(OrderStatus status) {
        return (status == null) ? null : QOrder.order.status.eq(status);
    }

    private BooleanExpression userNameContains(String userName) {
        return (userName == null || userName.isEmpty()) ? null : QUser.user.name.contains(userName);
    }

    private BooleanExpression documentNoContains(String documentNo) {
        return (documentNo == null || documentNo.isEmpty()) ? null : QOrder.order.documentNo.contains(documentNo);
    }

    private BooleanExpression remarkContains(String remark) {
        return (remark == null || remark.isEmpty()) ? null : QOrder.order.remark.contains(remark);
    }

    // 전표번호에서 날짜 부분만 잘라서 between 검색
    private BooleanExpression documentNoBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return null;

        String startStr = start.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String endStr = end.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        return Expressions.stringTemplate(
                "SUBSTRING_INDEX({0}, '-', 1)", QOrder.order.documentNo
        ).between(startStr, endStr);
    }

    // 정렬 조건 매핑
    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, QOrder order) {
        return pageable.getSort().stream()
                .map(sort -> switch (sort.getProperty()) {
                    case "deliveryDate" ->
                            sort.isAscending() ? order.deliveryDate.asc() : order.deliveryDate.desc();
                    case "documentNo" ->
                            sort.isAscending() ? order.documentNo.asc() : order.documentNo.desc();
                    case "createdAt" ->
                            sort.isAscending() ? order.createdAt.asc() : order.createdAt.desc();
                    default -> null;
                })
                .filter(Objects::nonNull)
                .toArray(OrderSpecifier[]::new);
    }
}
