package com.domino.smerp.productionplan.repository;

import com.domino.smerp.common.util.QuerydslUtils;
import com.domino.smerp.productionplan.ProductionPlan;
import com.domino.smerp.productionplan.QProductionPlan;
import com.domino.smerp.productionplan.constants.Status;
import com.domino.smerp.productionplan.dto.request.SearchProductionPlanRequest;
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
@Repository
@RequiredArgsConstructor
public class ProductionPlanQueryRepositoryImpl implements ProductionPlanQueryRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public Page<ProductionPlan> searchProductionPlans(
      final SearchProductionPlanRequest keyword,
      final Pageable pageable) {


    QProductionPlan productionPlan = QProductionPlan.productionPlan;

    BooleanExpression condition = QProductionPlan.productionPlan.isNotNull(); // 항상 참
    if (keyword.getTitle() != null && !keyword.getTitle().isEmpty()) {
      condition = condition.and(QProductionPlan.productionPlan.title.contains(keyword.getTitle()));
    }
    if (keyword.getStatus() != null) {
      condition = condition.and(QProductionPlan.productionPlan.status.eq(keyword.getStatus()));
    }


    // 정렬 컬럼 매핑
  Map<String, Path<? extends Comparable<?>>> sortMapping = Map.of(
    "title", productionPlan.title,
    "status", productionPlan.status,
    "qty", productionPlan.qty,
    "createdAt", productionPlan.createdAt
  );

  List<OrderSpecifier<?>> orders = QuerydslUtils.getSort(pageable.getSort(), sortMapping);
      if (orders.isEmpty()) {
  orders.add(productionPlan.id.desc()); // 기본 정렬
  }

  // 실제 데이터 조회
  List<ProductionPlan> content = queryFactory
    .selectFrom(productionPlan)
    .where(condition)
    .orderBy(orders.toArray(new OrderSpecifier[0]))
    .offset(pageable.getOffset())
    .limit(pageable.getPageSize())
    .fetch();

  // 카운트 쿼리
  JPAQuery<Long> countQuery = queryFactory
    .select(productionPlan.count())
    .from(productionPlan)
    .where(condition);

      return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  // 검색 조건 BooleanExpression
  private BooleanExpression titleContains(String title) {
  return (title == null || title.isEmpty()) ? null : QProductionPlan.productionPlan.title.contains(title);
  }

  private BooleanExpression statusEq(Status status) {
    return status == null ? null : QProductionPlan.productionPlan.status.eq(status);
  }

}
