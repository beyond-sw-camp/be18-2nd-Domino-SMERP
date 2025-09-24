package com.domino.smerp.workorder.repository;

import com.domino.smerp.common.util.QuerydslUtils;
import com.domino.smerp.workorder.WorkOrder;
import com.domino.smerp.workorder.QWorkOrder;
import com.domino.smerp.workorder.constants.Status;
import com.domino.smerp.workorder.dto.request.SearchWorkOrderRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class WorkOrderQueryRepositoryImpl implements WorkOrderQueryRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<WorkOrder> searchWorkOrders(SearchWorkOrderRequest request, Pageable pageable) {
    QWorkOrder workOrder = QWorkOrder.workOrder;

    BooleanBuilder builder = new BooleanBuilder();

    if (StringUtils.hasText(request.getItemName())) {
      builder.and(workOrder.item.name.eq(request.getItemName()));
    }

    if (request.getStatus() != null) {
      builder.and(workOrder.status.eq(request.getStatus()));
    }

    if (request.getProductionPlanId() != null) {
      builder.and(workOrder.productionPlan.id.eq(request.getProductionPlanId()));
    }

    // 정렬 컬럼 매핑
    Map<String, Path<? extends Comparable<?>>> sortMapping = Map.of(
        "itemName", workOrder.item.name,
        "status", workOrder.status,
        "productionPlanId", workOrder.productionPlan.id
    );

    List<OrderSpecifier<?>> orders = QuerydslUtils.getSort(pageable.getSort(), sortMapping);
    if (orders.isEmpty()) {
      orders.add(workOrder.id.desc()); // 기본 정렬
    }

    // 실제 데이터 조회
    List<WorkOrder> content = queryFactory
        .selectFrom(workOrder)
        .leftJoin(workOrder.item).fetchJoin()
        .leftJoin(workOrder.productionPlan).fetchJoin()
        .where(builder)
        .orderBy(orders.toArray(new OrderSpecifier[0]))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // 카운트 쿼리
    JPAQuery<Long> countQuery = queryFactory
        .select(workOrder.count())
        .from(workOrder)
        .where(builder);

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }


}
