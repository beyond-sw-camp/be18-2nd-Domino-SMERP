package com.domino.smerp.purchase.purchaseorder.repository.query;

import com.domino.smerp.common.util.QuerydslUtils;
import com.domino.smerp.purchase.purchaseorder.PurchaseOrder;
import com.domino.smerp.purchase.purchaseorder.QPurchaseOrder;
import com.domino.smerp.purchase.purchaseorder.dto.request.SearchPurchaseOrderRequest;
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
public class PurchaseOrderQueryRepositoryImpl implements PurchaseOrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PurchaseOrder> searchPurchaseOrder(SearchPurchaseOrderRequest condition, Pageable pageable) {
        QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;

        // 정렬 매핑
        Map<String, Path<? extends Comparable<?>>> sortMapping = Map.of(
                "poId", purchaseOrder.poId,
                "documentNo", purchaseOrder.documentNo,
                "companyName", purchaseOrder.requestOrder.client.companyName,
                "empNo", purchaseOrder.requestOrder.user.empNo,
                "warehouseName", purchaseOrder.warehouseName,
                "createdAt", purchaseOrder.createdAt,
                "updatedAt", purchaseOrder.updatedAt
        );

        // 정렬조건
        List<OrderSpecifier<?>> orders = QuerydslUtils.getSort(pageable.getSort(), sortMapping);

        if (orders.isEmpty()) {
            orders.add(purchaseOrder.createdAt.desc()); // 기본 정렬
        }

        // 결과 리스트
        List<PurchaseOrder> results = queryFactory
                .selectFrom(purchaseOrder)
                .join(purchaseOrder.requestOrder).fetchJoin()
                .join(purchaseOrder.requestOrder.client).fetchJoin()
                .join(purchaseOrder.requestOrder.user).fetchJoin()
                .where(
                        poIdEq(condition.getPoId()),
                        documentNoContains(condition.getDocumentNo()),
                        companyNameContains(condition.getCompanyName()),
                        empNoEq(condition.getEmpNo()),
                        warehouseNameContains(condition.getWarehouseName())
                )
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Count 쿼리
        JPAQuery<Long> countQuery = queryFactory
                .select(purchaseOrder.count())
                .from(purchaseOrder)
                .join(purchaseOrder.requestOrder)
                .join(purchaseOrder.requestOrder.client)
                .join(purchaseOrder.requestOrder.user)
                .where(
                        poIdEq(condition.getPoId()),
                        documentNoContains(condition.getDocumentNo()),
                        companyNameContains(condition.getCompanyName()),
                        empNoEq(condition.getEmpNo()),
                        warehouseNameContains(condition.getWarehouseName())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanExpression poIdEq(Long poId) {
        return poId != null ? QPurchaseOrder.purchaseOrder.poId.eq(poId) : null;
    }

    private BooleanExpression documentNoContains(String documentNo) {
        return (documentNo == null || documentNo.isEmpty()) ? null : QPurchaseOrder.purchaseOrder.documentNo.contains(documentNo);
    }

    private BooleanExpression companyNameContains(String companyName) {
        return (companyName == null || companyName.isEmpty()) ? null : QPurchaseOrder.purchaseOrder.requestOrder.client.companyName.contains(companyName);
    }

    private BooleanExpression empNoEq(String empNo) {
        return (empNo == null || empNo.isEmpty()) ? null : QPurchaseOrder.purchaseOrder.requestOrder.user.empNo.eq(empNo);
    }

    private BooleanExpression warehouseNameContains(final String warehouseName) {
        return (warehouseName == null || warehouseName.isEmpty()) ? null : QPurchaseOrder.purchaseOrder.warehouseName.contains(warehouseName);
    }

}
