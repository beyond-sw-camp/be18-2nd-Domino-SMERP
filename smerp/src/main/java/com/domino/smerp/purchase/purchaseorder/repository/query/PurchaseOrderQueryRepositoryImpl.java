package com.domino.smerp.purchase.purchaseorder.repository.query;

import com.domino.smerp.client.QClient;
import com.domino.smerp.common.util.QuerydslUtils;
import com.domino.smerp.item.QItem;
import com.domino.smerp.purchase.itemrequestorder.QItemRequestOrder;
import com.domino.smerp.purchase.purchaseorder.PurchaseOrder;
import com.domino.smerp.purchase.purchaseorder.QPurchaseOrder;
import com.domino.smerp.purchase.purchaseorder.dto.request.SearchPurchaseOrderRequest;
import com.domino.smerp.purchase.purchaseorder.dto.request.SearchSummaryPurchaseOrderRequest;
import com.domino.smerp.purchase.purchaseorder.dto.response.SummaryPurchaseOrderResponse;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
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
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class PurchaseOrderQueryRepositoryImpl implements PurchaseOrderQueryRepository {

    private final JPAQueryFactory queryFactory;

    // 구매 현황
    @Override
    public List<SummaryPurchaseOrderResponse> searchSummaryPurchaseOrders(SearchSummaryPurchaseOrderRequest condition, Pageable pageable) {
        QPurchaseOrder purchaseOrder = QPurchaseOrder.purchaseOrder;
        QClient client = QClient.client;
        QItemRequestOrder itemRequestOrder = QItemRequestOrder.itemRequestOrder;
        QItem item = QItem.item;

        return queryFactory
                .select(Projections.constructor(
                        SummaryPurchaseOrderResponse.class,
                        purchaseOrder.documentNo,
                        client.companyName,
                        item.name,
                        itemRequestOrder.qty,
                        itemRequestOrder.inboundUnitPrice,
                        Expressions.numberTemplate(BigDecimal.class,
                                "ROUND({0} * {1}, 2)",   // qty * inboundUnitPrice
                                itemRequestOrder.qty, itemRequestOrder.inboundUnitPrice),
                        purchaseOrder.remark
                ))
                .from(purchaseOrder)
                .join(purchaseOrder.requestOrder.client, client)
                .join(purchaseOrder.requestOrder.items, itemRequestOrder)
                .join(itemRequestOrder.item, item)
                .where(
                        companyNameContains(condition.getCompanyName()),
                        documentNoContains(condition.getDocumentNo()),
                        itemNameContains(condition.getItemName()),
                        remarkContains(condition.getRemark()),
                        qtyEq(condition.getQty()),
                        inboundUnitPriceEq(condition.getInboundUnitPrice()),
                        supplyAmountEq(condition.getSupplyAmount()),
                        documentNoBetween(condition.getStartDocDate(), condition.getEndDocDate())
                )
                .orderBy(purchaseOrder.documentNo.asc()) // 기본 정렬 (필요 시 pageable sort로 교체 가능)
                .fetch();
    }

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

    private BooleanExpression itemNameContains(String itemName) {
        return (itemName == null || itemName.isEmpty()) ? null : QItem.item.name.contains(itemName);
    }

    private BooleanExpression remarkContains(String remark) {
        return (remark == null || remark.isEmpty()) ? null : QPurchaseOrder.purchaseOrder.remark.contains(remark);
    }

    private BooleanExpression qtyEq(BigDecimal qty) {
        return (qty == null) ? null : QItemRequestOrder.itemRequestOrder.qty.eq(qty);  // ✅ 수정됨
    }

    private BooleanExpression inboundUnitPriceEq(BigDecimal inboundUnitPrice) {
        return (inboundUnitPrice == null) ? null : QItemRequestOrder.itemRequestOrder.inboundUnitPrice.eq(inboundUnitPrice);  // ✅ 수정됨
    }

    private BooleanExpression supplyAmountEq(BigDecimal supplyAmount) {
        return (supplyAmount == null) ? null :
                QItemRequestOrder.itemRequestOrder.qty.multiply(QItemRequestOrder.itemRequestOrder.inboundUnitPrice).eq(supplyAmount);  // ✅ 수정됨
    }

    private BooleanExpression documentNoBetween(LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            String startStr = start.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String endStr = end.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            return Expressions.stringTemplate(
                    "SUBSTRING_INDEX({0}, '-', 1)", QPurchaseOrder.purchaseOrder.documentNo
            ).between(startStr, endStr);
        } else if (start != null) {
            String startStr = start.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            return Expressions.stringTemplate(
                    "SUBSTRING_INDEX({0}, '-', 1)", QPurchaseOrder.purchaseOrder.documentNo
            ).goe(startStr);
        } else if (end != null) {
            String endStr = end.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            return Expressions.stringTemplate(
                    "SUBSTRING_INDEX({0}, '-', 1)", QPurchaseOrder.purchaseOrder.documentNo
            ).loe(endStr);
        }
        return null;
    }
}
