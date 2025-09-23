package com.domino.smerp.warehouse.repository;

import com.domino.smerp.common.util.QuerydslUtils;
import com.domino.smerp.warehouse.QWarehouse;
import com.domino.smerp.warehouse.Warehouse;
import com.domino.smerp.warehouse.constants.DivisionType;
import com.domino.smerp.warehouse.dto.request.SearchWarehouseRequest;
import com.domino.smerp.warehouse.dto.response.WarehouseResponse;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
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
public class WarehouseQueryRepositoryImpl implements WarehouseQueryRepository{
  private final JPAQueryFactory queryFactory;

  @Override
  public Page<Warehouse> searchWarehouses(final SearchWarehouseRequest keyword, final Pageable pageable) {
    QWarehouse warehouse = QWarehouse.warehouse;

    BooleanExpression condition = null;

    BooleanExpression nameCond = warehouseNameContains(keyword.getWarehouseName());
    BooleanExpression divisionCond = divisionTypeEq(keyword.getDivisionType());
    BooleanExpression activeCond = activeEq(keyword.isActive());
    BooleanExpression addressCond = addressContains(keyword.getAddress());
    BooleanExpression zipcodeCond = zipcodeContains(keyword.getZipcode());

    if (nameCond != null) condition = nameCond;
    if (divisionCond != null) condition = (condition == null ? divisionCond : condition.and(divisionCond));
    if (activeCond != null) condition = (condition == null ? activeCond : condition.and(activeCond));
    if (addressCond != null) condition = (condition == null ? addressCond : condition.and(addressCond));
    if (zipcodeCond != null) condition = (condition == null ? zipcodeCond : condition.and(zipcodeCond));

    // 정렬 컬럼 매핑
    Map<String, Path<? extends Comparable<?>>> sortMapping = Map.of(
        "name", warehouse.name,
        "divisionType", warehouse.divisionType,
        "active", warehouse.active,
        "zipcode", warehouse.zipcode,
        "createdAt", warehouse.createdAt
    );

    List<OrderSpecifier<?>> orders = QuerydslUtils.getSort(pageable.getSort(), sortMapping);
    if (orders.isEmpty()) {
      orders.add(warehouse.id.desc()); // 기본 정렬
    }

    // 실제 데이터 조회
    List<Warehouse> content = queryFactory
        .selectFrom(warehouse)
        .where(condition)
        .orderBy(orders.toArray(new OrderSpecifier[0]))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // 카운트 쿼리
    JPAQuery<Long> countQuery = queryFactory
        .select(warehouse.count())
        .from(warehouse)
        .where(condition);

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  // 검색 조건 BooleanExpression
  private BooleanExpression warehouseNameContains(String name) {
    return (name == null || name.isEmpty()) ? null : QWarehouse.warehouse.name.contains(name);
  }

  private BooleanExpression divisionTypeEq(DivisionType divisionType) {
    return divisionType == null ? null : QWarehouse.warehouse.divisionType.eq(divisionType);
  }

  private BooleanExpression activeEq(Boolean active) {
    return active == null ? null : QWarehouse.warehouse.active.eq(active);
  }

  private BooleanExpression addressContains(String address) {
    return (address == null || address.isEmpty()) ? null : QWarehouse.warehouse.address.contains(address);
  }

  private BooleanExpression zipcodeContains(String zipcode) {
    return (zipcode == null || zipcode.isEmpty()) ? null : QWarehouse.warehouse.zipcode.contains(zipcode);
  }

}
