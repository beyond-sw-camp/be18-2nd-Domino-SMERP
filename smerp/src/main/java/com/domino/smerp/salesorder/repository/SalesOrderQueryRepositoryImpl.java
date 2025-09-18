package com.domino.smerp.salesorder.repository;

import com.domino.smerp.client.QClient;
import com.domino.smerp.item.QItem;
import com.domino.smerp.itemorder.QItemOrder;
import com.domino.smerp.order.QOrder;
import com.domino.smerp.salesorder.QSalesOrder;
import com.domino.smerp.salesorder.SalesOrder;
import com.domino.smerp.salesorder.dto.request.SearchExcelSalesOrderRequest;
import com.domino.smerp.salesorder.dto.request.SearchSalesOrderRequest;
import com.domino.smerp.salesorder.dto.response.ExcelSalesOrderResponse;
import com.domino.smerp.user.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

        // ✅ DISTINCT 추가 → 중복 row 제거
        List<SalesOrder> results = queryFactory
                .selectFrom(so).distinct()
                .join(so.order, order).fetchJoin()
                .join(order.client, client).fetchJoin()
                .join(order.user, user).fetchJoin()
                .where(
                        companyNameContains(condition.getCompanyName(), client),
                        userNameContains(condition.getUserName(), user),
                        documentNoContains(condition.getDocumentNo(), so),
                        remarkContains(condition.getRemark(), so),
                        warehouseNameContains(condition.getWarehouseName(), so),
                        documentNoBetween(condition.getStartDocDate(), condition.getEndDocDate(), so)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리 (distinct 불필요)
        JPAQuery<Long> countQuery = queryFactory
                .select(so.count())
                .from(so)
                .join(so.order, order)
                .join(order.client, client)
                .join(order.user, user)
                .where(
                        companyNameContains(condition.getCompanyName(), client),
                        userNameContains(condition.getUserName(), user),
                        documentNoContains(condition.getDocumentNo(), so),
                        remarkContains(condition.getRemark(), so),
                        warehouseNameContains(condition.getWarehouseName(), so),
                        documentNoBetween(condition.getStartDocDate(), condition.getEndDocDate(), so)
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    @Override
    public List<ExcelSalesOrderResponse> searchExcelSalesOrder(SearchExcelSalesOrderRequest condition, Pageable pageable) {
        QSalesOrder salesOrder = QSalesOrder.salesOrder;
        QOrder order = QOrder.order;
        QClient client = QClient.client;
        QItemOrder itemOrder = QItemOrder.itemOrder;
        QItem item = QItem.item;

        return queryFactory
                .select(Projections.constructor(
                        ExcelSalesOrderResponse.class,
                        salesOrder.documentNo,
                        item.name,
                        itemOrder.qty,
                        itemOrder.specialPrice,
                        Expressions.numberTemplate(BigDecimal.class, "ROUND({0} * {1}, 2)",
                                itemOrder.qty, itemOrder.specialPrice),
                        Expressions.numberTemplate(BigDecimal.class,
                                "ROUND(({0} * {1}) * 0.1, 2)",
                                itemOrder.qty, itemOrder.specialPrice),
                        Expressions.numberTemplate(BigDecimal.class,
                                "ROUND(({0} * {1}) * 1.1, 2)",
                                itemOrder.qty, itemOrder.specialPrice),
                        client.companyName
                ))
                .from(salesOrder)
                .join(salesOrder.order, order)
                .join(order.orderItems, itemOrder)
                .join(itemOrder.item, item)
                .join(order.client, client)
                .where(
                        companyNameContains(condition.getClientName(), client),
                        documentNoContains(condition.getDocumentNo(), salesOrder),
                        itemNameContains(condition.getItemName(), item),
                        documentNoBetween(condition.getStartDocDate(), condition.getEndDocDate(), salesOrder)
                )
                .fetch();
    }

    // ===================== 검색 조건 =====================

    private BooleanExpression companyNameContains(String name, QClient client) {
        return (name == null || name.isEmpty()) ? null : client.companyName.contains(name);
    }

    private BooleanExpression userNameContains(String userName, QUser user) {
        return (userName == null || userName.isEmpty()) ? null : user.name.contains(userName);
    }

    private BooleanExpression documentNoContains(String documentNo, QSalesOrder so) {
        return (documentNo == null || documentNo.isEmpty()) ? null : so.documentNo.contains(documentNo);
    }

    private BooleanExpression remarkContains(String remark, QSalesOrder so) {
        return (remark == null || remark.isEmpty()) ? null : so.remark.contains(remark);
    }

    private BooleanExpression documentNoBetween(LocalDate start, LocalDate end, QSalesOrder so) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        if (start != null && end != null) {
            String startStr = start.format(formatter);
            String endStr = end.format(formatter);
            return so.documentNo.substring(0, 10).between(startStr, endStr);
        } else if (start != null) {
            String startStr = start.format(formatter);
            return so.documentNo.substring(0, 10).goe(startStr); // 시작일 이상
        } else if (end != null) {
            String endStr = end.format(formatter);
            return so.documentNo.substring(0, 10).loe(endStr);   // 종료일 이하
        } else {
            return null;
        }
    }

    private BooleanExpression itemNameContains(String itemName, QItem item) {
        return (itemName == null || itemName.isEmpty()) ? null : item.name.contains(itemName);
    }

    private BooleanExpression warehouseNameContains(String warehouseName, QSalesOrder so) {
        return (warehouseName == null || warehouseName.isEmpty()) ? null : so.warehouseName.contains(warehouseName);
    }
}
