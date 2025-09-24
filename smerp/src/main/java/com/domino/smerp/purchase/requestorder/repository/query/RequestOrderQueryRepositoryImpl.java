// file: src/main/java/com/domino/smerp/purchase/requestorder/repository/query/RequestOrderQueryRepositoryImpl.java
package com.domino.smerp.purchase.requestorder.repository.query;

import com.domino.smerp.common.util.QuerydslUtils;
import com.domino.smerp.purchase.requestorder.QRequestOrder;
import com.domino.smerp.purchase.requestorder.RequestOrder;
import com.domino.smerp.purchase.requestorder.constants.RequestOrderStatus;
import com.domino.smerp.purchase.requestorder.dto.request.SearchRequestOrderRequest;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RequestOrderQueryRepositoryImpl implements RequestOrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RequestOrder> searchRequestOrder(SearchRequestOrderRequest condition, Pageable pageable) {
        QRequestOrder requestOrder = QRequestOrder.requestOrder;

        // 정렬 매핑
        Map<String, Path<? extends Comparable<?>>> sortMapping = Map.of(
                "roId", requestOrder.roId,
                "documentNo", requestOrder.documentNo,
                "companyName", requestOrder.client.companyName,
                "empNo", requestOrder.user.empNo,
                "status", requestOrder.status,
                "createdAt", requestOrder.createdAt,
                "updatedAt", requestOrder.updatedAt
        );

        // 정렬조건
        List<OrderSpecifier<?>> orders = QuerydslUtils.getSort(pageable.getSort(), sortMapping);
        if (orders.isEmpty()) {
            orders.add(requestOrder.createdAt.desc()); // 기본 정렬
        }

        // 결과 리스트
        List<RequestOrder> results = queryFactory
                .selectFrom(requestOrder)
                .join(requestOrder.client).fetchJoin()
                .join(requestOrder.user).fetchJoin()
                .where(
                        roIdEq(condition.getRoId()),
                        documentNoContains(condition.getDocumentNo()),
                        companyNameContains(condition.getCompanyName()),
                        empNoEq(condition.getEmpNo()),
                        statusEq(condition.getStatus()),
                        remarkContains(condition.getRemark())
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(requestOrder.count())
                .from(requestOrder)
                .join(requestOrder.client)
                .join(requestOrder.user)
                .where(
                        roIdEq(condition.getRoId()),
                        documentNoContains(condition.getDocumentNo()),
                        companyNameContains(condition.getCompanyName()),
                        empNoEq(condition.getEmpNo()),
                        statusEq(condition.getStatus()),
                        remarkContains(condition.getRemark())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanExpression roIdEq(Long roId) {
        return roId != null ? QRequestOrder.requestOrder.roId.eq(roId) : null;
    }

    private BooleanExpression documentNoContains(String documentNo) {
        return (documentNo == null || documentNo.isEmpty()) ? null : QRequestOrder.requestOrder.documentNo.contains(documentNo);
    }

    private BooleanExpression companyNameContains(String companyName) {
        return (companyName == null || companyName.isEmpty()) ? null : QRequestOrder.requestOrder.client.companyName.contains(companyName);
    }

    private BooleanExpression empNoEq(String empNo) {
        return (empNo == null || empNo.isEmpty()) ? null : QRequestOrder.requestOrder.user.empNo.eq(empNo);
    }

    private BooleanExpression statusEq(RequestOrderStatus status) {
        return status != null ? QRequestOrder.requestOrder.status.eq(status) : null;
    }

    private BooleanExpression remarkContains(String remark) {
        return (remark == null || remark.isEmpty()) ? null : QRequestOrder.requestOrder.remark.contains(remark);
    }
}
