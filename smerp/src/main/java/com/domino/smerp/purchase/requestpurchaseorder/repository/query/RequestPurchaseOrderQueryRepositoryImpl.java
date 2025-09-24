package com.domino.smerp.purchase.requestpurchaseorder.repository.query;

import com.domino.smerp.common.util.QuerydslUtils;
import com.domino.smerp.purchase.requestpurchaseorder.QRequestPurchaseOrder;
import com.domino.smerp.purchase.requestpurchaseorder.RequestPurchaseOrder;
import com.domino.smerp.purchase.requestpurchaseorder.constants.RequestPurchaseOrderStatus;
import com.domino.smerp.purchase.requestpurchaseorder.dto.request.SearchRequestPurchaseOrderRequest;
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
public class RequestPurchaseOrderQueryRepositoryImpl implements RequestPurchaseOrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<RequestPurchaseOrder> searchRequestPurchaseOrder(SearchRequestPurchaseOrderRequest condition, Pageable pageable) {
        QRequestPurchaseOrder rpo = QRequestPurchaseOrder.requestPurchaseOrder;

        Map<String, Path<? extends Comparable<?>>> sortMapping = Map.of(
                "rpoId", rpo.rpoId,
                "documentNo", rpo.documentNo,
                "empNo", rpo.user.empNo,
                "status", rpo.status,
                "createdAt", rpo.createdAt,
                "updatedAt", rpo.updatedAt
        );

        List<OrderSpecifier<?>> orders = QuerydslUtils.getSort(pageable.getSort(), sortMapping);
        if (orders.isEmpty()) {
            orders.add(rpo.createdAt.desc());
        }

        List<RequestPurchaseOrder> results = queryFactory
                .selectFrom(rpo)
                .join(rpo.user).fetchJoin()
                .where(
                        rpoIdEq(condition.getRpoId()),
                        documentNoContains(condition.getDocumentNo()),
                        empNoEq(condition.getEmpNo()),
                        statusEq(condition.getStatus()),
                        remarkContains(condition.getRemark())
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(rpo.count())
                .from(rpo)
                .join(rpo.user)
                .where(
                        rpoIdEq(condition.getRpoId()),
                        documentNoContains(condition.getDocumentNo()),
                        empNoEq(condition.getEmpNo()),
                        statusEq(condition.getStatus()),
                        remarkContains(condition.getRemark())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanExpression rpoIdEq(Long rpoId) {
        return rpoId != null ? QRequestPurchaseOrder.requestPurchaseOrder.rpoId.eq(rpoId) : null;
    }

    private BooleanExpression documentNoContains(String documentNo) {
        return (documentNo == null || documentNo.isEmpty()) ? null
                : QRequestPurchaseOrder.requestPurchaseOrder.documentNo.contains(documentNo);
    }

    private BooleanExpression empNoEq(String empNo) {
        return (empNo == null || empNo.isEmpty()) ? null
                : QRequestPurchaseOrder.requestPurchaseOrder.user.empNo.eq(empNo);
    }

    private BooleanExpression statusEq(RequestPurchaseOrderStatus status) {
        return status != null ? QRequestPurchaseOrder.requestPurchaseOrder.status.eq(status) : null;
    }

    private BooleanExpression remarkContains(String remark) {
        return (remark == null || remark.isEmpty()) ? null
                : QRequestPurchaseOrder.requestPurchaseOrder.remark.contains(remark);
    }
}
