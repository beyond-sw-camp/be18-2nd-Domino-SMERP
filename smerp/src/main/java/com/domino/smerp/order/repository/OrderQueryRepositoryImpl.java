package com.domino.smerp.order.repository;

import com.domino.smerp.client.QClient;
import com.domino.smerp.item.QItem;
import com.domino.smerp.itemorder.QItemOrder;
import com.domino.smerp.order.Order;
import com.domino.smerp.order.QOrder;
import com.domino.smerp.order.constants.OrderStatus;
import com.domino.smerp.order.dto.request.SearchOrderRequest;
import com.domino.smerp.order.dto.request.SearchSummaryOrderRequest;
import com.domino.smerp.order.dto.request.SearchSummaryReturnOrderRequest;
import com.domino.smerp.order.dto.response.SummaryOrderResponse;
import com.domino.smerp.order.dto.response.SummaryReturnOrderResponse;
import com.domino.smerp.user.QUser;
import com.querydsl.core.types.OrderSpecifier;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> searchOrders(SearchOrderRequest condition, Pageable pageable) {
        QOrder order = QOrder.order;
        QClient client = QClient.client;
        QUser user = QUser.user;

        List<Order> results = queryFactory
                .selectFrom(order).distinct() // 중복 제거
                .join(order.client, client).fetchJoin()
                .join(order.user, user).fetchJoin()
                .leftJoin(order.orderItems, QItemOrder.itemOrder).fetchJoin()
                .leftJoin(QItemOrder.itemOrder.item, QItem.item).fetchJoin()
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
                .orderBy(getOrderSpecifiers(pageable, order))
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

    @Override
    public List<SummaryOrderResponse> searchSummaryOrders(SearchSummaryOrderRequest condition, Pageable pageable) {
        QOrder order = QOrder.order;
        QClient client = QClient.client;
        QItemOrder itemOrder = QItemOrder.itemOrder;
        QItem item = QItem.item;

        return queryFactory
                .select(Projections.constructor(
                        SummaryOrderResponse.class,
                        order.documentNo,
                        client.companyName,
                        item.name,
                        itemOrder.qty,
                        itemOrder.specialPrice,
                        Expressions.numberTemplate(BigDecimal.class, "ROUND({0} * {1}, 2)",
                                itemOrder.qty, itemOrder.specialPrice), // 소수점 2자리
                        order.remark
                ))
                .from(order)
                .join(order.client, client)
                .join(order.orderItems, itemOrder)
                .join(itemOrder.item, item)
                .where(
                        companyNameContains(condition.getCompanyName()),
                        documentNoContains(condition.getDocumentNo()),
                        itemNameContains(condition.getItemName()),
                        remarkContains(condition.getRemark()),
                        qtyEq(condition.getQty()),
                        specialPriceEq(condition.getSpecialPrice()),
                        supplyAmountEq(condition.getSupplyAmount()),
                        documentNoBetween(condition.getStartDocDate(), condition.getEndDocDate())
                )
                .orderBy(getOrderSpecifiers(pageable, order, client, itemOrder, item))
                .fetch();
    }

    @Override
    public List<SummaryReturnOrderResponse> searchSummaryReturnOrders(SearchSummaryReturnOrderRequest condition, Pageable pageable) {
        QOrder order = QOrder.order;
        QClient client = QClient.client;
        QItemOrder itemOrder = QItemOrder.itemOrder;
        QItem item = QItem.item;

        return queryFactory
                .select(Projections.constructor(
                        SummaryReturnOrderResponse.class,
                        order.documentNo,
                        client.companyName,
                        item.name,
                        itemOrder.qty.abs(),
                        itemOrder.specialPrice,
                        Expressions.numberTemplate(BigDecimal.class,
                                "ROUND(ABS({0} * {1}) * 1.1, 2)",
                                itemOrder.qty, itemOrder.specialPrice), // 환불금액: 절댓값
                        order.remark
                ))
                .from(order)
                .join(order.client, client)
                .join(order.orderItems, itemOrder)
                .join(itemOrder.item, item)
                .where(
                        documentNoContains(condition.getDocumentNo()),
                        companyNameContains(condition.getCompanyName()),
                        itemNameContains(condition.getItemName()),
                        qtyEq(condition.getQty()),
                        specialPriceEq(condition.getSpecialPrice()),
                        refundAmountEq(condition.getRefundAmount()),
                        remarkContains(condition.getRemark()),
                        documentNoBetween(condition.getStartDocDate(), condition.getEndDocDate()), // ✅ 추가
                        isReturnOrder(order.documentNo) // 반품만 조회
                )
                .orderBy(getOrderSpecifiers(pageable, order, client, itemOrder, item))
                .fetch()
                .stream()
                .map(r -> SummaryReturnOrderResponse.builder()
                        .documentNo(r.getDocumentNo())
                        .companyName(r.getCompanyName())
                        .itemName(r.getItemName())
                        .qty(r.getQty())
                        .specialPrice(r.getSpecialPrice())
                        .refundAmount(r.getRefundAmount().setScale(2, RoundingMode.HALF_UP)) // ✅ 여기서 강제 처리
                        .remark(r.getRemark())
                        .build()
                )
                .toList();
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

    private BooleanExpression itemNameContains(String itemName) {
        return (itemName == null || itemName.isEmpty()) ? null : QItem.item.name.contains(itemName);
    }

    private BooleanExpression qtyEq(BigDecimal qty) {
        return (qty == null) ? null : QItemOrder.itemOrder.qty.eq(qty);
    }

    private BooleanExpression specialPriceEq(BigDecimal specialPrice) {
        return (specialPrice == null) ? null : QItemOrder.itemOrder.specialPrice.eq(specialPrice);
    }

    private BooleanExpression supplyAmountEq(BigDecimal supplyAmount) {
        return (supplyAmount == null) ? null : QItemOrder.itemOrder.qty.multiply(QItemOrder.itemOrder.specialPrice).eq(supplyAmount);
    }

    private BooleanExpression refundAmountEq(BigDecimal refundAmount) {
        return (refundAmount == null) ? null :
                QItemOrder.itemOrder.qty.multiply(QItemOrder.itemOrder.specialPrice).abs().eq(refundAmount);
    }

    private BooleanExpression isReturnOrder(com.querydsl.core.types.dsl.StringPath documentNo) {
        return documentNo.contains("(-"); // "-" 포함된 전표번호만 반품
    }

    // 전표번호에서 날짜 부분만 잘라서 between 검색
    private BooleanExpression documentNoBetween(LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            String startStr = start.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String endStr = end.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            return Expressions.stringTemplate(
                    "SUBSTRING_INDEX({0}, '-', 1)", QOrder.order.documentNo
            ).between(startStr, endStr);
        } else if (start != null) {
            String startStr = start.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            return Expressions.stringTemplate(
                    "SUBSTRING_INDEX({0}, '-', 1)", QOrder.order.documentNo
            ).goe(startStr);  // start 이상
        } else if (end != null) {
            String endStr = end.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            return Expressions.stringTemplate(
                    "SUBSTRING_INDEX({0}, '-', 1)", QOrder.order.documentNo
            ).loe(endStr);    // end 이하
        }
        return null;
    }


    // 목록 정렬 조건 매핑
    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, QOrder order) {
        return pageable.getSort().stream()
                .map(sort -> switch (sort.getProperty()) {
                    case "deliveryDate" -> sort.isAscending() ? order.deliveryDate.asc() : order.deliveryDate.desc();
                    case "documentNo" -> sort.isAscending() ? order.documentNo.asc() : order.documentNo.desc();
                    case "createdAt" -> sort.isAscending() ? order.createdAt.asc() : order.createdAt.desc();
                    default -> null;
                })
                .filter(Objects::nonNull)
                .toArray(OrderSpecifier[]::new);
    }

    // 현황 정렬 조건 매핑
    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, QOrder order, QClient client, QItemOrder itemOrder, QItem item) {
        return pageable.getSort().stream()
                .map(sort -> switch (sort.getProperty()) {
                    case "documentNo" -> sort.isAscending() ? order.documentNo.asc() : order.documentNo.desc();
                    case "companyName" -> sort.isAscending() ? client.companyName.asc() : client.companyName.desc();
                    case "itemName" -> sort.isAscending() ? item.name.asc() : item.name.desc();
                    case "qty" -> sort.isAscending() ? itemOrder.qty.asc() : itemOrder.qty.desc();
                    case "specialPrice" ->
                            sort.isAscending() ? itemOrder.specialPrice.asc() : itemOrder.specialPrice.desc();
                    case "supplyAmount" -> sort.isAscending() ? itemOrder.qty.multiply(itemOrder.specialPrice).asc()
                            : itemOrder.qty.multiply(itemOrder.specialPrice).desc();
                    case "remark" -> sort.isAscending() ? order.remark.asc() : order.remark.desc();
                    default -> null;
                })
                .filter(Objects::nonNull)
                .toArray(OrderSpecifier[]::new);
    }
}
